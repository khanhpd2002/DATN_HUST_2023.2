package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;

public interface GarageServiceRepositoryCustom {
    PagingDataResponse findAll(Long garageId, String name, String code, Long garageServiceTypeId, Integer pageSize, Integer pageNumber);

    PagingDataResponse getServiceApplyForAllGarage(String name, String code, Long garageServiceTypeId, Integer pageSize, Integer pageNumber);

    Boolean checkExistGarageServiceByCodeAndGarageId(String code, Long garageId, Long currentId);
}
