package vn.com.cardoctor.garage_service.services.garages;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.ConfigPrice;
import vn.com.cardoctor.garage_service.entities.CustomerType;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.entities.garages.GarageServiceType;
import vn.com.cardoctor.garage_service.enums.ProductTypeEnum;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageServiceRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageServiceResponse;
import vn.com.cardoctor.garage_service.repositories.ConfigPriceRepository;
import vn.com.cardoctor.garage_service.repositories.CustomerTypeRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceTypeRepository;
import vn.com.cardoctor.garage_service.utils.TextUtil;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class GarageServiceService {
    private final GarageServiceTypeRepository garageServiceTypeRepository;
    private final GarageServiceRepository garageServiceRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final ConfigPriceRepository configPriceRepository;
    private final GarageRepository garageRepository;

    public PagingDataResponse getServiceApplyForAllGarage(String name, String code, Long garageServiceTypeId,
                                                          Integer pageSize, Integer pageNumber) {
        return this.garageServiceRepository.getServiceApplyForAllGarage(name, code, garageServiceTypeId, pageSize, pageNumber);
    }

    public List<GarageServiceResponse> bulkCreate(Long garageId, List<GarageServiceRequest> requests) throws ApiException {
        List<GarageServiceResponse> responses = new ArrayList<>();
        for (GarageServiceRequest request : requests) {
            GarageServiceResponse garageServiceResponse = this.create(garageId, request);
            garageServiceResponse.setInstanceKey(request.getInstanceKey());
            responses.add(garageServiceResponse);
        }
        return responses;
    }

    public GarageServiceResponse create(Long garageId, GarageServiceRequest garageServiceRequest) throws ApiException {
        this.validateRequest(garageServiceRequest);
        if (Boolean.TRUE.equals(this.garageServiceRepository.checkExistGarageServiceByCodeAndGarageId(garageServiceRequest.getCode(), garageId, null))) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin mã dịch vụ đã tồn tại");
        }
        GarageService garageService = new GarageService();
        garageService.setCode(garageServiceRequest.getCode());
        garageService.setName(garageServiceRequest.getName());
        garageService.setDescription(garageServiceRequest.getDescription());
        garageService.setGarageId(garageId);
        garageService.setIsActive(garageServiceRequest.getIsActive() != null ? garageServiceRequest.getIsActive() : Boolean.TRUE);
        if (garageServiceRequest.getGarageServiceTypeId() != null) {
            garageService.setGarageServiceTypeId(garageServiceRequest.getGarageServiceTypeId());
        } else {
            GarageServiceType garageServiceType = new GarageServiceType();
            garageServiceType.setName(garageServiceRequest.getGarageServiceTypeName());
            garageServiceType.setDescription(garageServiceRequest.getGarageServiceTypeName());
            garageServiceType.setGarageId(garageId);
            garageServiceType = this.garageServiceTypeRepository.save(garageServiceType);
            garageService.setGarageServiceTypeId(garageServiceType.getId());
        }
        garageService = this.garageServiceRepository.save(garageService);
        List<ConfigPrice> configPrices = new ArrayList<>();
        List<CustomerType> customerTypes = this.customerTypeRepository.findByGarageIdIsNull();
        if (garageId != null) {
            for (CustomerType customerType : customerTypes) {
                ConfigPrice configPrice = new ConfigPrice();
                configPrice.setGarageServiceId(garageService.getId());
                configPrice.setCustomerTypeId(customerType.getId());
                configPrice.setPrice(BigDecimal.valueOf(0));
                configPrice.setGarageId(garageId);
                configPrice.setType(ProductTypeEnum.SERVICE.getCode());
                configPrices.add(configPrice);
            }
            this.configPriceRepository.saveAll(configPrices);
        } else {
            GarageService garageServiceInThread = garageService;
            Thread thread = new Thread(() -> this.createDefaultServicePriceForAllGarage(garageServiceInThread.getId(), customerTypes));
            thread.start();
        }
        GarageServiceResponse response = new GarageServiceResponse();
        response.setGarageServiceId(garageService.getId());
        response.setGarageServiceTypeId(garageService.getGarageServiceTypeId());
        return response;
    }

    public PagingDataResponse findAll(Long garageId, String name, String code, Long garageServiceTypeId, Integer pageSize, Integer pageNumber) {
        return this.garageServiceRepository.findAll(garageId, name, code, garageServiceTypeId, pageSize, pageNumber);
    }

    public GarageService findById(Long garageId, Long id) throws ApiException {
        Optional<GarageService> oGarageService = this.garageServiceRepository.findById(id);
        if (oGarageService.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        return oGarageService.get();
    }

    public GarageService update(Long garageId, Long garageServiceId, GarageServiceRequest garageServiceRequest) throws ApiException {
        this.validateRequest(garageServiceRequest);
        Optional<GarageService> oGarageService = this.garageServiceRepository.findById(garageServiceId);
        if (oGarageService.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại dịch vụ");
        }
        if (this.garageServiceRepository.checkExistGarageServiceByCodeAndGarageId(garageServiceRequest.getCode(), garageId, garageServiceId)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin mã dịch vụ đã tồn tại");
        }
        GarageService garageService = oGarageService.get();
        garageService.setCode(garageServiceRequest.getCode());
        garageService.setName(garageServiceRequest.getName());
        garageService.setDescription(garageServiceRequest.getDescription());
        garageService.setGarageId(garageId);
        if (garageServiceRequest.getGarageServiceTypeId() != null) {
            garageService.setGarageServiceTypeId(garageServiceRequest.getGarageServiceTypeId());
        } else {
            GarageServiceType garageServiceType = new GarageServiceType();
            garageServiceType.setName(garageServiceRequest.getGarageServiceTypeName());
            garageServiceType.setDescription(garageServiceRequest.getGarageServiceTypeName());
            garageServiceType.setGarageId(garageId);
            garageServiceType = this.garageServiceTypeRepository.save(garageServiceType);
            garageService.setGarageServiceTypeId(garageServiceType.getId());
        }
        return this.garageServiceRepository.save(garageService);
    }

    private void validateRequest(GarageServiceRequest garageServiceRequest) throws ApiException {
        if (TextUtil.isNullOrEmpty(garageServiceRequest.getName())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin tên dịch vụ");
        }
        if (TextUtil.isNullOrEmpty(garageServiceRequest.getCode())) {
            garageServiceRequest.setCode(null);
        }
        if (TextUtil.isNullOrEmpty(garageServiceRequest.getGarageServiceTypeName()) && garageServiceRequest.getGarageServiceTypeId() == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin loại hình dịch vụ");
        }
    }

    private void createDefaultServicePriceForAllGarage(Long serviceId, List<CustomerType> customerTypes) {
        List<Garage> garages = this.garageRepository.findAll();
        List<ConfigPrice> configPrices = new ArrayList<>();
        for (Garage garage : garages) {
            for (CustomerType customerType : customerTypes) {
                ConfigPrice configPrice = new ConfigPrice();
                configPrice.setGarageServiceId(serviceId);
                configPrice.setCustomerTypeId(customerType.getId());
                configPrice.setPrice(BigDecimal.valueOf(0));
                configPrice.setGarageId(garage.getId());
                configPrice.setType(ProductTypeEnum.SERVICE.getCode());
                configPrices.add(configPrice);
            }
        }
        this.configPriceRepository.saveAll(configPrices);
    }
}
