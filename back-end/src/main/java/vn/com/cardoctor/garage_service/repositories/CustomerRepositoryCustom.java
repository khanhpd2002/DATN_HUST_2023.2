package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;

public interface CustomerRepositoryCustom {
    PagingDataResponse findAllCustomer(Long garageId, Long customerTypeId, String customerName, String phoneNumber,
                                       Integer pageSize, Integer pageNumber);
}
