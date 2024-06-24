package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.AdjustmentInventoryQuantityHistory;

@Repository
public interface AdjustmentInventoryQuantityHistoryRepository extends JpaRepository<AdjustmentInventoryQuantityHistory, Long> {
}
