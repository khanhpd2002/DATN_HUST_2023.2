package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.dtos.CarDto;
import vn.com.cardoctor.garage_service.models.dtos.HistoryRepairOrderDto;

import java.util.List;

public interface CarRepositoryCustom {
    PagingDataResponse findAllCar(Long carBrandId, Long carModelId, Long carYearId, Long carVersionId, String licensePlate,
                                  Long garageId, Long customerTypeId, Long customerId, String filter, Integer pageSize, Integer pageNumber);

    List<HistoryRepairOrderDto> findAllHistoryRepairOrder(Long carId);

    List<CarDto> findCarDtoByCustomerId(Long customerId);

    Boolean checkExistCarByLicensePlateAndGarageId(String licensePlate, Long garageId, Long currentId);

    Boolean checkExistCarByVinNumberAndGarageId(String vinNumber, Long garageId, Long currentId);
}
