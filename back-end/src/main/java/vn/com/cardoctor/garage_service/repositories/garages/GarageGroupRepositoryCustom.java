package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;

public interface GarageGroupRepositoryCustom {
    PagingDataResponse findGarageGroup(String name, String phone, String website, String email, String taxCode,
                                       Integer status, Integer pageSize, Integer pageNumber);
}
