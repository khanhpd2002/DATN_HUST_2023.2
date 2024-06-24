package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;

import java.util.Date;

public interface InventoryHistoryRepositoryCustom {
    PagingDataResponse findAllInventoryHistory(Long inventoryId, Date fromDate, Date toDate, Integer pageSize, Integer pageNumber);

    PagingDataResponse getDetailHistory(Long historyId, Integer pageSize, Integer pageNumber);
}
