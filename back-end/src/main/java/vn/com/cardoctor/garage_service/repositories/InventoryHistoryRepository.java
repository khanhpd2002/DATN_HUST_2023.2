package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.InventoryHistory;

import java.util.List;

public interface InventoryHistoryRepository extends CrudRepository<InventoryHistory, Long>, InventoryHistoryRepositoryCustom {
    List<InventoryHistory> findAllByInventoryId(Long inventoryId);
}
