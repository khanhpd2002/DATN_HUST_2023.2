package vn.com.cardoctor.garage_service.services;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.Car;
import vn.com.cardoctor.garage_service.models.dtos.CarInfoDetailDto;
import vn.com.cardoctor.garage_service.models.dtos.HistoryRepairOrderDto;
import vn.com.cardoctor.garage_service.models.requests.car.CarRequest;
import vn.com.cardoctor.garage_service.repositories.CarRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarService {
    @Autowired
    CarRepository carRepository;

    public PagingDataResponse findAllCar(Long carBrandId, Long carModelId, Long carYearId, Long carVersionId, String licensePlate,
                                         Long garageId, Long customerTypeId, Long customerId, String filter, Integer pageSize, Integer pageNumber) {
        return this.carRepository.findAllCar(carBrandId, carModelId, carYearId, carVersionId, licensePlate,
                garageId, customerTypeId, customerId, filter, pageSize, pageNumber);
    }

    public BaseResponse<CarInfoDetailDto> findById(Long garageId, Long carId) throws ApiException {
        Optional<Car> oCar = this.carRepository.findById(carId);
        if (oCar.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn taị thông tin xe này");
        }
        CarInfoDetailDto car = new CarInfoDetailDto();
        car.setId(oCar.get().getId());
        car.setCarVersionId(oCar.get().getCarVersionId());
        car.setCarBrandId(oCar.get().getCarBrandId());
        car.setCarYearId(oCar.get().getCarYearId());
        car.setCarModelId(oCar.get().getCarModelId());
        car.setCarName(oCar.get().getCarName());
        car.setGarageId(oCar.get().getGarageId());
        car.setDriverCarId(oCar.get().getDriverCarId());
        car.setLicensePlate(oCar.get().getLicensePlate());
        car.setVinNumber(oCar.get().getVinNumber());
        car.setCustomerId(oCar.get().getCustomerId());
        car.setListHistoryRepairOrders(this.findAllHistoryRepairOrder(carId));

        return new BaseResponse<>(1, "Success", car);
    }

    public Long create(Long garageId, CarRequest carRequest) throws ApiException {
        this.validRequest(carRequest);
        if (this.carRepository.checkExistCarByLicensePlateAndGarageId(carRequest.getLicensePlate(), garageId, null)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với biển số này");
        }
        if (carRequest.getVinNumber() != null && !carRequest.getVinNumber().isBlank()) {
            if (this.carRepository.checkExistCarByVinNumberAndGarageId(carRequest.getVinNumber(), garageId, null)) {
                throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với mã số vin này");
            }
        }
        Car car = new Car();
        this.convertRequestToEntity(car, carRequest);
        car.setGarageId(garageId);
        car = this.carRepository.save(car);
        return car.getId();
    }

    public BaseResponse update(Long garageId, CarRequest carRequest, Long carId) throws ApiException {
        this.validRequest(carRequest);
        Optional<Car> oCar = this.carRepository.findById(carId);
        if (oCar.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin xe");
        }
        Car car = oCar.get();
        if (Boolean.TRUE.equals(this.carRepository.checkExistCarByLicensePlateAndGarageId(carRequest.getLicensePlate(), garageId, car.getId()))) {
            throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với biển số này");
        }
        if (carRequest.getVinNumber() != null && (Boolean.TRUE.equals(this.carRepository.checkExistCarByVinNumberAndGarageId(carRequest.getVinNumber(), garageId, car.getId())))) {
                throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với mã số vin này");

        }
        this.convertRequestToEntity(car, carRequest);
        car.setGarageId(garageId);
        this.carRepository.save(car);
        return new BaseResponse();
    }

    public Long updateBasicInfoCar(Long carId, CarRequest carRequest) throws ApiException {
        this.validBasicInfoRequest(carRequest);
        Optional<Car> oCar = this.carRepository.findById(carId);
        if (oCar.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin xe");
        }
        Car car = oCar.get();
        car.setCarBrandId(carRequest.getCarBrandId());
        car.setCarModelId(carRequest.getCarModelId());
        car.setCarYearId(carRequest.getCarYearId());
        car.setCarVersionId(carRequest.getCarVersionId());
        this.carRepository.save(car);
        return carId;
    }

    public void convertRequestToEntity(Car car, CarRequest carRequest) {
        car.setCarName(carRequest.getCarName());
        car.setCarBrandId(carRequest.getCarBrandId());
        car.setCarModelId(carRequest.getCarModelId());
        car.setCarYearId(carRequest.getCarYearId());
        car.setCarVersionId(carRequest.getCarVersionId());
        car.setLicensePlate(carRequest.getLicensePlate());
        car.setVinNumber(carRequest.getVinNumber());
        car.setDriverCarId(carRequest.getDriverCarId());
        car.setCustomerId(carRequest.getCustomerId());
    }

    public void validRequest(CarRequest carRequest) throws ApiException {
        if (carRequest.getLicensePlate().isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thiếu thông tin biển số xe");
        }
    }

    public void validBasicInfoRequest(CarRequest carRequest) throws ApiException {
        if (carRequest.getCarBrandId() == null || carRequest.getCarBrandId().equals(0L)) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thiếu thông tin hãng xe");
        }
        if (carRequest.getCarModelId() == null || carRequest.getCarModelId().equals(0L)) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thiếu thông tin dòng xe xe");
        }
        if (carRequest.getCarYearId() == null || carRequest.getCarYearId().equals(0L)) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thiếu thông tin đời xe");
        }
        if (carRequest.getCarVersionId() == null || carRequest.getCarVersionId().equals(0L)) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thiếu thông tin phiên bản xe");
        }
    }

    public List<HistoryRepairOrderDto> findAllHistoryRepairOrder(Long carId) {
        return this.carRepository.findAllHistoryRepairOrder(carId);
    }
}
