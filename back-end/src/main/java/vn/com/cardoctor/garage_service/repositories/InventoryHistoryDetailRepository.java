package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.InventoryHistoryDetail;

@Repository
public interface InventoryHistoryDetailRepository extends CrudRepository<InventoryHistoryDetail, Long>, InventoryHistoryDetailRepositoryCustom {
    @Transactional
    @Modifying
    @Query(name = "delete from inventory_histories_detail where inventory_history_id = :inventoryHistoryId", nativeQuery = true)
    void deleteAllByInventoryHistoryId(Long inventoryHistoryId);
}
