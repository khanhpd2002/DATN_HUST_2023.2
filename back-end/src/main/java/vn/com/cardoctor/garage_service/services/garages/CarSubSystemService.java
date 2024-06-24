package vn.com.cardoctor.garage_service.services.garages;


import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.car_sub_system.CarSubSystemRequest;
import vn.com.cardoctor.garage_service.models.responses.car_sub_system.CarSubSystemResponse;
import vn.com.cardoctor.garage_service.repositories.garages.CarSubSystemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarSubSystemService {
    @Autowired
    CarSubSystemRepository carSubSystemRepository;

    public PagingDataResponse findAllCarSubSystem(String name, String description, Integer status,
                                                  Integer pageSize, Integer pageNumber) throws ApiException {
        return this.carSubSystemRepository.findAllCarSubSystem(name, description, status, pageSize, pageNumber);
    }

    public CarSubSystemResponse findById(Long id) throws ApiException {
        Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(id);
        if (oCarSubSystem.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Car System is not exists");
        }
        CarSubSystemResponse carSubSystemResponse = new CarSubSystemResponse();
        this.convertEntityToResponse(oCarSubSystem.get(), carSubSystemResponse);
        return carSubSystemResponse;
    }
    public BaseResponse create(CarSubSystemRequest carSubSystemRequest) throws ApiException {
        if (carSubSystemRequest.getName() == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Invalid Request");
        }
        CarSubSystem carSubSystem = new CarSubSystem();
        this.convertRequestToEntity(carSubSystem, carSubSystemRequest);
        carSubSystem.setStatus(EntityStatus.PENDING.getCode());
        this.carSubSystemRepository.save(carSubSystem);
        return new BaseResponse(1, "Create Car system success");
    }

    public BaseResponse update(CarSubSystemRequest carSubSystemRequest, Long id) throws ApiException {
        Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(id);
        if (oCarSubSystem.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Car system is not exists");
        }
        CarSubSystem carSubSystem = oCarSubSystem.get();
        this.convertRequestToEntity(carSubSystem, carSubSystemRequest);
        this.carSubSystemRepository.save(carSubSystem);
        return new BaseResponse(1, "Update Car system success");
    }

    public BaseResponse accept(Long id) throws ApiException {
        Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(id);
        if (oCarSubSystem.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        CarSubSystem carSubSystem = oCarSubSystem.get();
        carSubSystem.setStatus(EntityStatus.ACCEPTED.getCode());
        this.carSubSystemRepository.save(carSubSystem);
        return new BaseResponse(1, "Accept Car system success");
    }

    public BaseResponse acceptList(List<Long> ids) throws ApiException {
        List<CarSubSystem> listCarSubSystem = this.carSubSystemRepository.findByListId(ids);
        for (CarSubSystem carSubSystem : listCarSubSystem) {
            carSubSystem.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.carSubSystemRepository.saveAll(listCarSubSystem);
        return new BaseResponse(1, "Accept success");
    }

    public BaseResponse reject(Long id) throws ApiException {
        Optional<CarSubSystem> oCarSubSystem = this.carSubSystemRepository.findById(id);
        if (oCarSubSystem.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        CarSubSystem carSubSystem = oCarSubSystem.get();
        carSubSystem.setStatus(EntityStatus.REJECTED.getCode());
        this.carSubSystemRepository.save(carSubSystem);
        return new BaseResponse(1, "Reject Car system success");
    }

    public BaseResponse rejectList(List<Long> ids) throws ApiException {
        List<CarSubSystem> listCarSubSystem = this.carSubSystemRepository.findByListId(ids);
        for (CarSubSystem carSubSystem : listCarSubSystem) {
            carSubSystem.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.carSubSystemRepository.saveAll(listCarSubSystem);
        return new BaseResponse(1, "Reject success");
    }

    public void convertRequestToEntity(CarSubSystem carSubSystem, CarSubSystemRequest carSubSystemRequest) {
        carSubSystem.setName(carSubSystemRequest.getName());
        carSubSystem.setDescription(carSubSystemRequest.getDescription());
    }

    public void convertEntityToResponse(CarSubSystem carSubSystem, CarSubSystemResponse carSubSystemResponse) {
        carSubSystemResponse.setId(carSubSystem.getId());
        carSubSystemResponse.setName(carSubSystem.getName());
        carSubSystemResponse.setStatus(carSubSystem.getStatus());
        carSubSystemResponse.setDescription(carSubSystem.getDescription());
    }
}

