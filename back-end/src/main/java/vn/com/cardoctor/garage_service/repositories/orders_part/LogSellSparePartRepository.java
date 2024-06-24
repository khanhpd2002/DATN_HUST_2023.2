package vn.com.cardoctor.garage_service.repositories.orders_part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.orders_part.LogSellSparePart;

import java.util.Optional;

@Repository
public interface LogSellSparePartRepository extends JpaRepository<LogSellSparePart, Long>, JpaSpecificationExecutor<LogSellSparePart> {
    Optional<LogSellSparePart> findFirstBySellSparePartIdOrderByLogVersionDesc(Long sellSparePartId);
}
