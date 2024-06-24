package vn.com.cardoctor.garage_service.services;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.ConfigPrice;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.enums.ProductTypeEnum;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;
import vn.com.cardoctor.garage_service.models.requests.product.ConfigPriceRequest;
import vn.com.cardoctor.garage_service.models.responses.product.ConfigPriceResponse;
import vn.com.cardoctor.garage_service.repositories.CustomerTypeRepository;
import vn.com.cardoctor.garage_service.repositories.ConfigPriceRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigPriceService {
    @Autowired
    ConfigPriceRepository configPriceRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerTypeRepository customerTypeRepository;

    @Autowired
    GarageServiceRepository garageServiceRepository;

    public PagingDataResponse findAllConfigPrice(Long garageId, Long productType, String productCode,
                                                  String productName, Integer pageSize, Integer pageNumber) {
        return this.configPriceRepository.findAllConfigPrice(garageId, productType, productCode, productName, pageSize, pageNumber);
    }

    public ConfigPriceDto findByCustomerTypeAndProductAndService(Long customerTypeId, Long modelId, Integer type) {
        ConfigPriceDto configPriceDto = this.configPriceRepository.findByCustomerTypeAndProductAndService(customerTypeId, modelId, type);
        return configPriceDto;
    }

    public ConfigPriceResponse findById(Long garageId, Long modelId, Integer type) throws ApiException {
        ConfigPriceResponse configPriceResponse = new ConfigPriceResponse();
        configPriceResponse.setType(type);
//        List<ConfigPrice> configPrices = new ArrayList<>();
        if (type.equals(ProductTypeEnum.SPARE_PART.getCode())) {
//            configPrices = this.configPriceRepository.findAllByProductId(modelId);
            Optional<Product> oProduct = this.productRepository.findById(modelId);
            if (oProduct.isEmpty()) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
            }
            Product product = oProduct.get();
            configPriceResponse.setProductId(product.getId());
            configPriceResponse.setProductName(product.getName());
            configPriceResponse.setProductCode(product.getCode());
            List<ConfigPriceDto> configPriceDtos = this.configPriceRepository.findConfigPriceByProductAndService(modelId, null);
            configPriceResponse.setConfigPrices(configPriceDtos);
        }
        if (type.equals(ProductTypeEnum.SERVICE.getCode())) {
//            configPrices = this.configPriceRepository.findAllByGarageServiceId(modelId);
            Optional<GarageService> oGarageService = this.garageServiceRepository.findById(modelId);
            if (oGarageService.isEmpty()) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
            }
            GarageService garageService = oGarageService.get();
            configPriceResponse.setGarageServiceId(garageService.getId());
            configPriceResponse.setGarageServiceName(garageService.getName());
            configPriceResponse.setGarageServiceCode(garageService.getCode());
            List<ConfigPriceDto> configPriceDtos = this.configPriceRepository.findConfigPriceByProductAndService(null, modelId);
            configPriceResponse.setConfigPrices(configPriceDtos);
        }
        return configPriceResponse;
    }

    public Long create(Long garageId, ConfigPriceRequest configPriceRequest, Integer type) throws ApiException {
        ConfigPrice configPrice = new ConfigPrice();
        this.convertRequestToEntity(configPrice, configPriceRequest);
        configPrice.setGarageId(garageId);
        configPrice = this.configPriceRepository.save(configPrice);
        return configPrice.getProductId();
    }

    public Long update(Long garageId, ConfigPriceRequest configPriceRequest, Long modelId, Integer type) throws ApiException {
        if (configPriceRequest.getModelId() == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin sản phẩm");
        }
        List<ConfigPrice> configPrices = new ArrayList<>();
        if (type.equals(ProductTypeEnum.SPARE_PART.getCode())) {
            configPrices = configPriceRepository.findAllByProductId(modelId);
        }
        if (type.equals(ProductTypeEnum.SERVICE.getCode())) {
            configPrices = configPriceRepository.findAllByGarageServiceId(modelId);
        }
        for (int i = 0; i < configPrices.size(); i++) {
            configPrices.get(i).setPrice(configPriceRequest.getConfigPrices().get(i).getPrice());
        }
        this.configPriceRepository.saveAll(configPrices);
        return modelId;
    }

    public BaseResponse delete(List<Long> productPriceIds) throws ApiException {
        this.configPriceRepository.deleteProductPrice(productPriceIds);
        return new BaseResponse();
    }

    public void convertRequestToEntity(ConfigPrice configPrice, ConfigPriceRequest configPriceRequest) {
        configPrice.setType(configPriceRequest.getType());
        if (configPriceRequest.getType().equals(ProductTypeEnum.SPARE_PART.getCode())) {
            configPrice.setProductId(configPriceRequest.getModelId());
        }
        if (configPriceRequest.getType().equals(ProductTypeEnum.SERVICE.getCode())) {
            configPrice.setGarageServiceId(configPriceRequest.getModelId());
        }
    }
}
