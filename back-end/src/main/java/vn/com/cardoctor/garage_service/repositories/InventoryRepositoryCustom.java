package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;

public interface InventoryRepositoryCustom {
    PagingDataResponse findAllInventory(Long garageId, String name, Integer pageSize, Integer pageNumber);
}
