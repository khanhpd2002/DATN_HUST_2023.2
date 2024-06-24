package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;

public interface TagRepositoryCustom {
    PagingDataResponse findAllTag(String name, String description, Integer status,
                                  Integer pageSize, Integer pageNumber);
}
