package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;

public interface InsuranceRepositoryCustom {
    PagingDataResponse findAllInsurance(String name, String description, Integer status,
                                  Integer pageSize, Integer pageNumber);
}
