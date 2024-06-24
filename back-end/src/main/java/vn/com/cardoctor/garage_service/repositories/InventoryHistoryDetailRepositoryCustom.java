package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDetailDto;

import java.util.List;

public interface InventoryHistoryDetailRepositoryCustom {
    List<InventoryHistoryDetailDto> findHistoryDetail(Long inventoryHistoryId, boolean isDifferentQuantity);
}
