package vn.com.cardoctor.garage_service.repositories.orders;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.orders.QuotationInfo;

import java.util.List;

@Repository
public interface QuotationInfoRepository extends CrudRepository<QuotationInfo, Long>, QuotationInfoRepositoryCustom {

    @Modifying
    @Transactional
    @Query(value = "delete from quotation_infos qt where qt.quotation_id = :quotationId", nativeQuery = true)
    void deleteByQuotationId(Long quotationId);

    @Modifying
    @Transactional
    @Query(value = "delete from quotation_infos qt where qt.quotation_id = :quotationId and qt.type = :type", nativeQuery = true)
    void deleteByQuotationIdAndType(Long quotationId, Integer type);

    List<QuotationInfo> findAllByQuotationId(Long quotationId);

    List<QuotationInfo> findAllByQuotationIdAndType(Long quotationId, Integer type);
}
