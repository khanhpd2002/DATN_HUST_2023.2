package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageBookingRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageInfoResponse;

import java.util.List;

public interface GarageRepositoryCustom {
    PagingDataResponse findAllGarage(String code, String name, Long provinceId, Long districtId, Long wardId, String address,
                                     String contactPointName, String contactPointPhone,
                                     Integer status, List<Long> carSubSystems, List<Long> rescues, List<Long> insurances, Integer pageSize, Integer pageNumber);

    PagingDataResponse searchGarageToBooking(GarageBookingRequest garageBookingRequest, Integer pageSize, Integer pageNumber);

    GarageInfoResponse findGarageInfoById(Long id);

    void insertGarage(GarageRequest garageRequest);
}
