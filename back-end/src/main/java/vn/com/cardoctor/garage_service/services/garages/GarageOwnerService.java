package vn.com.cardoctor.garage_service.services.garages;

import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.Generator;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageGarageOwner;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.garage_owner.GarageOwnerRequest;
import vn.com.cardoctor.garage_service.models.responses.garage_owner.CheckExistGarageOwnerResponse;
import vn.com.cardoctor.garage_service.models.responses.garage_owner.GarageOwnerDto;
import vn.com.cardoctor.garage_service.repositories.GeneratorRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageGarageOwnerRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageOwnerRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class GarageOwnerService {
    @Value("${default.password}")
    String defaultPassword;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageOwnerRepository garageOwnerRepository;

    @Autowired
    GarageGarageOwnerRepository garageGarageOwnerRepository;

    @Autowired
    GeneratorRepository generatorRepository;

    public PagingDataResponse findAllGarageOwner(String name, String userName, String phone, String email, Integer gender,
                                                 Date fromDateBirthday, Date toDateBirthday, Integer status, Integer pageSize, Integer pageNumber) {
        return this.garageOwnerRepository.findAllGarageOwner(name, userName, phone, email, gender,
                fromDateBirthday, toDateBirthday, status, pageSize, pageNumber);
    }

    public GarageOwnerDto findById(Long id) throws ApiException {
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findById(id);
        if (oGarageOwner.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage owner is not exists");
        }
        GarageOwner garageOwner = oGarageOwner.get();
        GarageOwnerDto garageOwnerDto = new GarageOwnerDto();
        garageOwnerDto.setId(garageOwner.getId());
        garageOwnerDto.setName(garageOwner.getName());
        garageOwnerDto.setCode(garageOwner.getCode());
        garageOwnerDto.setUserName(garageOwner.getUserName());
        garageOwnerDto.setPhone(garageOwner.getPhone());
        garageOwnerDto.setEmail(garageOwner.getEmail());
        garageOwnerDto.setIdentifiedCard(garageOwner.getIdentifiedCard());
        garageOwnerDto.setDateOfIssue(garageOwner.getDateOfIssue());
        garageOwnerDto.setPlaceOfIssue(garageOwner.getPlaceOfIssue());
        garageOwnerDto.setGender(garageOwner.getGender());
        garageOwnerDto.setBirthday(garageOwner.getBirthday());
        garageOwnerDto.setAvatar(garageOwner.getAvatar());
        garageOwnerDto.setKeycloakId(garageOwner.getKeycloakId());
        garageOwnerDto.setStatus(garageOwner.getStatus());
        List<GarageGarageOwner> garageGarageOwners = this.garageGarageOwnerRepository.findAllByGarageOwnerId(garageOwner.getId());
        List<Garage> garages = new ArrayList<>();
        for (GarageGarageOwner garageGarageOwner : garageGarageOwners) {
            Optional<Garage> oGarage = this.garageRepository.findById(garageGarageOwner.getGarageId());
            if (oGarage.isPresent()) {
                Garage garage = oGarage.get();
                garages.add(garage);
            }
        }
        garageOwnerDto.setGarages(garages);
        return garageOwnerDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(GarageOwnerRequest garageOwnerRequest) throws ApiException {
        if (garageOwnerRequest.getPhone() == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Invalid request");
        }
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findByUserName(garageOwnerRequest.getUserName());
        if (oGarageOwner.isPresent()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Đã tồn tại chủ Gara với tài khoản này");
        }
        log.info("Current time 1 is {}", LocalDateTime.now());
        GarageOwner garageOwner = new GarageOwner();
        garageOwner.setName(garageOwnerRequest.getName());
        String code = String.format("GO%09d", Long.valueOf(this.getGeneratorValue("GO")));
        garageOwner.setCode(code);
        garageOwner.setUserName(garageOwnerRequest.getUserName());
        garageOwner.setPhone(garageOwnerRequest.getPhone());
        garageOwner.setEmail(garageOwnerRequest.getEmail());
        garageOwner.setIdentifiedCard(garageOwnerRequest.getIdentifiedCard());
        garageOwner.setDateOfIssue(garageOwnerRequest.getDateOfIssue());
        garageOwner.setPlaceOfIssue(garageOwnerRequest.getPlaceOfIssue());
        garageOwner.setGender(garageOwnerRequest.getGender());
        garageOwner.setBirthday(garageOwnerRequest.getBirthday());
        garageOwner.setAvatar(garageOwnerRequest.getAvatar());
        garageOwner.setStatus(EntityStatus.PENDING.getCode());
        // Set keycloak
//        String keycloakId = this.createUserKeycloak(garageOwnerRequest.getUserName(), garageOwner.getName(), "GARAGE_OWNER");
//        garageOwner.setKeycloakId(keycloakId);
        garageOwner = this.garageOwnerRepository.save(garageOwner);
        List<GarageGarageOwner> garageGarageOwners = new ArrayList<>();
        for (Garage garage : garageOwnerRequest.getGarages()) {
            GarageGarageOwner garageGarageOwner = new GarageGarageOwner();
            garageGarageOwner.setGarageOwnerId(garageOwner.getId());
            garageGarageOwner.setGarageId(garage.getId());
            garageGarageOwners.add(garageGarageOwner);
        }
        this.garageGarageOwnerRepository.saveAll(garageGarageOwners);
        return garageOwner.getId();
    }

    public Long update(GarageOwnerRequest garageOwnerRequest, Long id) throws ApiException {
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findById(id);
        if (oGarageOwner.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage owner is not exists");
        }
        GarageOwner garageOwner = oGarageOwner.get();
        garageOwner.setName(garageOwnerRequest.getName());
        garageOwner.setUserName(garageOwnerRequest.getUserName());
        garageOwner.setPhone(garageOwnerRequest.getPhone());
        garageOwner.setEmail(garageOwnerRequest.getEmail());
        garageOwner.setIdentifiedCard(garageOwnerRequest.getIdentifiedCard());
        garageOwner.setDateOfIssue(garageOwnerRequest.getDateOfIssue());
        garageOwner.setPlaceOfIssue(garageOwnerRequest.getPlaceOfIssue());
        garageOwner.setGender(garageOwnerRequest.getGender());
        garageOwner.setBirthday(garageOwnerRequest.getBirthday());
        garageOwner.setAvatar(garageOwnerRequest.getAvatar());
        garageOwner.setStatus(EntityStatus.PENDING.getCode());
        garageOwner = this.garageOwnerRepository.save(garageOwner);
        this.garageGarageOwnerRepository.deleteAllByGarageOwnerId(garageOwner.getId());
        List<GarageGarageOwner> garageGarageOwners = new ArrayList<>();
        for (Garage garage : garageOwnerRequest.getGarages()) {
            GarageGarageOwner garageGarageOwner = new GarageGarageOwner();
            garageGarageOwner.setGarageOwnerId(garageOwner.getId());
            garageGarageOwner.setGarageId(garage.getId());
            garageGarageOwners.add(garageGarageOwner);
        }
        this.garageGarageOwnerRepository.saveAll(garageGarageOwners);
        return id;
    }

    public Long accept(Long id) throws ApiException {
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findById(id);
        if (oGarageOwner.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage owner is not exists");
        }
        GarageOwner garageOwner = oGarageOwner.get();
        if (garageOwner.getStatus().equals(EntityStatus.ACCEPTED.getCode())) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Cannot accept");
        }
        garageOwner.setStatus(EntityStatus.ACCEPTED.getCode());
//        this.enableUserKeycloak(garageOwner.getKeycloakId());
        this.garageOwnerRepository.save(garageOwner);
        return id;
    }

    public String acceptList(List<Long> ids) {
        List<GarageOwner> listGarageOwner = this.garageOwnerRepository.findByListId(ids);
        for (GarageOwner garageOwner : listGarageOwner) {
            garageOwner.setStatus(EntityStatus.ACCEPTED.getCode());
//            this.enableUserKeycloak(garageOwner.getKeycloakId());
        }
        this.garageOwnerRepository.saveAll(listGarageOwner);
        return "Success";
    }

    public Long reject(Long id) throws ApiException {
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findById(id);
        if (oGarageOwner.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Garage owner is not exists");
        }
        GarageOwner garageOwner = oGarageOwner.get();
        if (garageOwner.getStatus().equals(EntityStatus.REJECTED.getCode())) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Cannot reject");
        }
        garageOwner.setStatus(EntityStatus.REJECTED.getCode());
//        this.disableUserKeycloak(garageOwner.getKeycloakId());
        this.garageOwnerRepository.save(garageOwner);
        return id;
    }

    public String rejectList(List<Long> ids) {
        List<GarageOwner> listGarageOwner = this.garageOwnerRepository.findByListId(ids);
        for (GarageOwner garageOwner : listGarageOwner) {
            garageOwner.setStatus(EntityStatus.REJECTED.getCode());
//            this.disableUserKeycloak(garageOwner.getKeycloakId());
        }
        this.garageOwnerRepository.saveAll(listGarageOwner);
        return "Success";
    }

    public List<CheckExistGarageOwnerResponse> checkExistGarageOwners(List<String> phoneNumbers) {
        List<CheckExistGarageOwnerResponse> response = new ArrayList<>();
        for (String phoneNumber : phoneNumbers) {
            CheckExistGarageOwnerResponse result = new CheckExistGarageOwnerResponse();
            result.setPhoneNumber(phoneNumber);
            Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findByPhone(phoneNumber);
            if (oGarageOwner.isEmpty()) {
                result.setIsExist(false);
            } else {
                result.setIsExist(true);
            }
            response.add(result);
        }
        return response;
    }

    protected String getGeneratorValue(String code) {
        Long suffixNumber = null;
        Optional<Generator> oGenerator = this.generatorRepository.findByCode(code);
        if (oGenerator.isPresent()) {
            try {
                suffixNumber = Long.parseLong(oGenerator.get().getValue()) + 1;
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            suffixNumber = 1L;
        }
        if (oGenerator.isPresent()) {
            Generator generator = oGenerator.get();
            generator.setValue(suffixNumber.toString());
            this.generatorRepository.save(generator);
        }
        return suffixNumber.toString();
    }
}
