package vn.com.cardoctor.garage_service.repositories.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.orders.QuotationProductLog;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationProductLogRepository extends JpaRepository<QuotationProductLog, Long>, JpaSpecificationExecutor<QuotationProductLog> {
    Optional<QuotationProductLog> findFirstByQuotationIdOrderByLogVersionDesc(Long quotationId);
}
