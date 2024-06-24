package vn.com.cardoctor.garage_service.repositories.orders;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.orders.Quotation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuotationRepository extends CrudRepository<Quotation, Long> {
    Optional<Quotation> findByRepairOrderId(long id);

    @Modifying
    @Transactional
    @Query(value = "delete from quotations q where q.repair_order_id = :repairOrderId", nativeQuery = true)
    void deleteByRepairOrderId(Long repairOrderId);
}
