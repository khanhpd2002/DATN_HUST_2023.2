package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;

public interface CustomerTypeRepositoryCustom {
    PagingDataResponse findAllByGarageId(Long garageId, String name, Integer pageSize, Integer pageNumber);

    PagingDataResponse findAll(String name, Integer pageSize, Integer pageNumber);
}
