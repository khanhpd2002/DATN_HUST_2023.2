package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.entities.QuotationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationServiceRepository extends CrudRepository<QuotationService, Long>, QuotationServiceRepositoryCustom {
    @Modifying
    void deleteByQuotationId(Long quotationId);
}
