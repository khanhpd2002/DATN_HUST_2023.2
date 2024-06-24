package vn.com.cardoctor.garage_service.services.garages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.CustomFieldValue;
import vn.com.cardoctor.garage_service.entities.Inventory;
import vn.com.cardoctor.garage_service.entities.garages.*;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.dtos.CustomFieldDto;
import vn.com.cardoctor.garage_service.models.requests.custom_field.CustomFieldValueRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageBookingRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.UpdateLogoRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageInfoResponse;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageResponse;
import vn.com.cardoctor.garage_service.repositories.CustomFieldRepository;
import vn.com.cardoctor.garage_service.repositories.CustomFieldValueRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;
import vn.com.cardoctor.garage_service.repositories.garages.*;
import vn.com.cardoctor.garage_service.services.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class GarageService extends BaseService {
    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageCarSubSystemRepository garageCarSubSystemRepository;

    @Autowired
    GarageOwnerRepository garageOwnerRepository;

    @Autowired
    GarageGarageOwnerRepository garageGarageOwnerRepository;

    @Autowired
    CarSubSystemRepository carSubSystemRepository;

    @Autowired
    CustomFieldRepository customFieldRepository;

    @Autowired
    CustomFieldValueRepository customFieldValueRepository;

    @Autowired
    GarageRescueRepository garageRescueRepository;

    @Autowired
    RescueRepository rescueRepository;

    @Autowired
    GarageInsuranceRepository garageInsuranceRepository;

    @Autowired
    InsuranceRepository insuranceRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ModelMapper modelMapper;

    public PagingDataResponse findAllGarage(String code, String name, Long provinceId, Long districtId, Long wardId, String address,
                                            String contactPointName, String contactPointPhone,
                                            Integer status, List<Long> carSubSystems, List<Long> rescues, List<Long> insurances, Integer pageSize, Integer pageNumber) {
        return this.garageRepository.findAllGarage(code, name, provinceId, districtId, wardId, address, contactPointName, contactPointPhone,
                status, carSubSystems, rescues, insurances, pageSize, pageNumber);
    }

    public PagingDataResponse searchGarageToBooking(GarageBookingRequest garageBookingRequest, Integer pageSize, Integer pageNumber) {
        return this.garageRepository.searchGarageToBooking(garageBookingRequest, pageSize, pageNumber);
    }

    public List<Garage> findByGarageOwner(Long garageOwnerId) {
        List<GarageGarageOwner> garageGarageOwners = this.garageGarageOwnerRepository.findAllByGarageOwnerId(garageOwnerId);
        List<Garage> garages = new ArrayList<>();
        for (GarageGarageOwner garageGarageOwner : garageGarageOwners) {
            Optional<Garage> oGarage = this.garageRepository.findById(garageGarageOwner.getGarageId());
            if (oGarage.isEmpty()) {
                Garage garage = new Garage();
                garages.add(garage);
            } else {
                Garage garage = oGarage.get();
                garages.add(garage);
            }
        }
        return garages;
    }

    public GarageResponse findById(Long id) throws ApiException, JsonProcessingException {
        Optional<Garage> oGarage = this.garageRepository.findById(id);
        if (oGarage.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage is not exists");
        }
        GarageResponse garageResponse = new GarageResponse();
        garageResponse.setGarage(oGarage.get());

        List<CarSubSystem> carSubSystems = new ArrayList<>();
        List<GarageCarSubSystem> garageCarSubSystems = this.garageCarSubSystemRepository.findAllByGarageId(id);
        for (GarageCarSubSystem garageCarSubSystem : garageCarSubSystems) {
            Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(garageCarSubSystem.getCarSubSystemId());
            oCarSubSystem.ifPresent(carSubSystems::add);
        }

        List<Rescue> rescues = new ArrayList<>();
        List<GarageRescue> garageRescues = this.garageRescueRepository.findAllByGarageId(id);
        for (GarageRescue garageRescue : garageRescues) {
            Optional<Rescue> oRescue = this.rescueRepository.findById(garageRescue.getRescueId());
            oRescue.ifPresent(rescues::add);
        }

        List<Insurance> insurances = new ArrayList<>();
        List<GarageInsurance> garageInsurances = this.garageInsuranceRepository.findAllByGarageId(id);
        for (GarageInsurance garageInsurance : garageInsurances) {
            Optional<Insurance> oInsurance = this.insuranceRepository.findById(garageInsurance.getInsuranceId());
            oInsurance.ifPresent(insurances::add);
        }

        garageResponse.setCarSubSystems(carSubSystems);
        garageResponse.setRescues(rescues);
        garageResponse.setInsurances(insurances);
        List<CustomFieldDto> customFieldDtos = this.customFieldRepository.getCustomField("garage", id);
        garageResponse.setCustomFields(customFieldDtos);
        return garageResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(GarageRequest garageRequest) throws ApiException {
        if (garageRequest.getContactPointPhone() == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Invalid Request");
        }
        Garage garage = new Garage();
        this.convertRequestToEntity(garage, garageRequest);
        String code = String.format("G%09d", Long.valueOf(this.getGeneratorValue("G")));
        garage.setCode(code);
        if (!garageRequest.getImages().isEmpty()) {
            String images = new Gson().toJson(garageRequest.getImages());
            garage.setImages(images);
        }
        garage = this.garageRepository.save(garage);

        Inventory inventory = new Inventory();
        inventory.setGarageId(garage.getId());
        inventory.setName("Kho của " + garage.getName());
        inventory.setDescription("Kho của " + garage.getName());
        this.inventoryRepository.save(inventory);

        // Set Car Sub System for garage
        if (garageRequest.getCarSubSystems() != null) {
            List<GarageCarSubSystem> garageCarSubSystems = new ArrayList<>();
            List<CarSubSystem> carSubSystems = garageRequest.getCarSubSystems();
            for (CarSubSystem carSubSystem : carSubSystems) {
                GarageCarSubSystem garageCarSubSystem = new GarageCarSubSystem();
                garageCarSubSystem.setGarageId(garage.getId());
                garageCarSubSystem.setCarSubSystemId(carSubSystem.getId());
                garageCarSubSystems.add(garageCarSubSystem);
            }
            this.garageCarSubSystemRepository.saveAll(garageCarSubSystems);
        }

        // Set type of rescue for garage
        if (garageRequest.getRescues() != null) {
            List<GarageRescue> garageRescues = new ArrayList<>();
            List<Rescue> rescues = garageRequest.getRescues();
            for (Rescue rescue : rescues) {
                GarageRescue garageRescue = new GarageRescue();
                garageRescue.setGarageId(garage.getId());
                garageRescue.setRescueId(rescue.getId());
                garageRescues.add(garageRescue);
            }
            this.garageRescueRepository.saveAll(garageRescues);
        }
        // Set Insurance for garage
        if (garageRequest.getInsurances() != null) {
            List<GarageInsurance> garageInsurances = new ArrayList<>();
            List<Insurance> insurances = garageRequest.getInsurances();
            for (Insurance insurance : insurances) {
                GarageInsurance garageInsurance = new GarageInsurance();
                garageInsurance.setGarageId(garage.getId());
                garageInsurance.setInsuranceId(insurance.getId());
                garageInsurances.add(garageInsurance);
            }
            this.garageInsuranceRepository.saveAll(garageInsurances);
        }
        return garage.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(GarageRequest garageRequest, Long id) throws ApiException {
        Optional<Garage> oGarage = this.garageRepository.findById(id);
        if (oGarage.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage is not exists");
        }
        Garage garage = oGarage.get();
        this.convertRequestToEntity(garage, garageRequest);
        if (!garageRequest.getImages().isEmpty()) {
            String images = new Gson().toJson(garageRequest.getImages());
            garage.setImages(images);
        }
        this.garageRepository.save(garage);

        this.customFieldValueRepository.deleteByModelId(id);
        List<CustomFieldValueRequest> customFieldValueRequests = garageRequest.getCustomFieldValueRequests();
        List<CustomFieldValue> customFieldValues = new ArrayList<>();
        for (CustomFieldValueRequest customFieldValueRequest : customFieldValueRequests) {
            CustomFieldValue customFieldValue = new CustomFieldValue();
            customFieldValue.setCustomFieldId(customFieldValueRequest.getCustomFieldId());
            customFieldValue.setTextValue(customFieldValueRequest.getTextValue());
            customFieldValue.setOptionValue(customFieldValueRequest.getOptionValue());
            customFieldValues.add(customFieldValue);
        }

        // Delete and Re-set Car Sub System for garage
        this.garageCarSubSystemRepository.deleteAllByGarageId(garage.getId());
        if (garageRequest.getCarSubSystems() != null) {
            List<GarageCarSubSystem> garageCarSubSystems = new ArrayList<>();
            List<CarSubSystem> carSubSystems = garageRequest.getCarSubSystems();
            for (CarSubSystem carSubSystem : carSubSystems) {
                GarageCarSubSystem garageCarSubSystem = new GarageCarSubSystem();
                garageCarSubSystem.setGarageId(garage.getId());
                garageCarSubSystem.setCarSubSystemId(carSubSystem.getId());
                garageCarSubSystems.add(garageCarSubSystem);
            }
            this.garageCarSubSystemRepository.saveAll(garageCarSubSystems);
        }

//        // Delete and Re-set type of rescue for garage
        this.garageRescueRepository.deleteAllByGarageId(garage.getId());
        if (garageRequest.getRescues() != null) {
            List<GarageRescue> garageRescues = new ArrayList<>();
            List<Rescue> rescues = garageRequest.getRescues();
            for (Rescue rescue : rescues) {
                GarageRescue garageRescue = new GarageRescue();
                garageRescue.setGarageId(garage.getId());
                garageRescue.setRescueId(rescue.getId());
                garageRescues.add(garageRescue);
            }
            this.garageRescueRepository.saveAll(garageRescues);
        }

//        // Delete and Re-set insurance for garage
        this.garageInsuranceRepository.deleteAllByGarageId(garage.getId());
        if (garageRequest.getInsurances() != null) {
            List<GarageInsurance> garageInsurances = new ArrayList<>();
            List<Insurance> insurances = garageRequest.getInsurances();
            for (Insurance insurance : insurances) {
                GarageInsurance garageInsurance = new GarageInsurance();
                garageInsurance.setGarageId(garage.getId());
                garageInsurance.setInsuranceId(insurance.getId());
                garageInsurances.add(garageInsurance);
            }
            this.garageInsuranceRepository.saveAll(garageInsurances);
        }

        this.customFieldValueRepository.saveAll(customFieldValues);
        return id;
    }

    public String acceptList(List<Long> ids) {
        List<Garage> listGarage = this.garageRepository.findByListId(ids);
        for (Garage garage : listGarage) {
            garage.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.garageRepository.saveAll(listGarage);
        return "Duyệt thành công";
    }

    public String rejectList(List<Long> ids) {
        List<Garage> listGarage = this.garageRepository.findByListId(ids);
        for (Garage garage : listGarage) {
            garage.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.garageRepository.saveAll(listGarage);
        return "Từ chối thành công";
    }

    public Long updateLogo(Long garageId, UpdateLogoRequest request) throws ApiException {
        Optional<Garage> oGarage = this.garageRepository.findById(garageId);
        if (oGarage.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage is not exists");
        }
        Garage garage = oGarage.get();
        garage.setLogo(request.getLogo());
        this.garageRepository.save(garage);
        return garage.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertGarage(GarageRequest garageRequest) {
        this.garageRepository.insertGarage(garageRequest);
    }

    public GarageInfoResponse findGarageInfoById(Long id) throws ApiException {
        Optional<Garage> oGarage = this.garageRepository.findById(id);
        if (oGarage.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        return this.garageRepository.findGarageInfoById(id);
    }

    public Long updateGarageInfo(Long id, Map<String, Object> additionalInformation) throws ApiException {
        Optional<Garage> oGarage = this.garageRepository.findById(id);
        if (oGarage.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Garage garage = oGarage.get();
        String information = new Gson().toJson(additionalInformation);
        garage.setAdditionalInformation(information);
        this.garageRepository.save(garage);
        return id;
    }

    public void convertRequestToEntity(Garage garage, GarageRequest garageRequest) {
        garage.setName(garageRequest.getName());
        garage.setContactPointName(garageRequest.getContactPointName());
        garage.setContactPointPhone(garageRequest.getContactPointPhone());
        garage.setContactPointEmail(garageRequest.getContactPointEmail());
        garage.setAvatar(garageRequest.getAvatar());
        garage.setContractFromDate(garageRequest.getContractFromDate());
        garage.setContractToDate(garageRequest.getContractToDate());
        garage.setContractStatus(garageRequest.getContractStatus());
        garage.setLatitude(garageRequest.getLatitude());
        garage.setLongitude(garageRequest.getLongitude());
        garage.setAddress(garageRequest.getAddress());
        garage.setProvinceId(garageRequest.getProvinceId());
        garage.setDistrictId(garageRequest.getDistrictId());
        garage.setWardId(garageRequest.getWardId());
        garage.setMaxCapacity(garageRequest.getMaxCapacity());
        garage.setCarLift(garageRequest.getCarLift());
        garage.setMaxTonnage(garageRequest.getMaxTonnage());
        garage.setNumberGatheringPoint(garageRequest.getNumberGatheringPoint());
        garage.setGarageGroupId(garageRequest.getGarageGroupId());
        garage.setOpen(garageRequest.getOpen());
        garage.setClose(garageRequest.getClose());
        garage.setSupportSos(garageRequest.getSupportSos());
        garage.setServiceTime(garageRequest.getServiceTime());
        garage.setGarageType(garageRequest.getGarageType());
        garage.setDescription(garageRequest.getDescription());
        garage.setIsVerified(garageRequest.getIsVerified());
        garage.setStatus(EntityStatus.PENDING.getCode());
    }
}
