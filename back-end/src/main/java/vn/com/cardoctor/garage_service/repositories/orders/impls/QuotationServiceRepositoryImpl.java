package vn.com.cardoctor.garage_service.repositories.orders.impls;

import vn.com.cardoctor.garage_service.models.dtos.QuotationServiceDto;
import vn.com.cardoctor.garage_service.repositories.orders.QuotationServiceRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotationServiceRepositoryImpl implements QuotationServiceRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuotationServiceDto> getQuotationService(Long quotationId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("SELECT qs.*, ");
        jpql.append("REPLACE(JSON_EXTRACT(content, '$.name'), '\"', '') as name, ");
        jpql.append("JSON_EXTRACT(content, '$.garage_id') as garage_id, ");
        jpql.append("JSON_EXTRACT(content, '$.warranty_period') as warranty_period, ");
        jpql.append("JSON_EXTRACT(content, '$.price') as price, ");
        jpql.append("REPLACE(JSON_EXTRACT(content, '$.description'), '\"', '') as description, ");
        jpql.append("JSON_EXTRACT(content, '$.status') as status ");
        jpql.append("FROM service_histories  sh");
        jpql.append("   JOIN quotation_services qs ON qs.service_id = sh.service_id ");
        jpql.append("   AND qs.service_version = sh.version");
        jpql.append("   AND qs.quotation_id = :quotationId");

        params.put("quotationId", quotationId);
        Query q = entityManager.createNativeQuery(jpql.toString(), QuotationServiceDto.class);
        params.forEach(q::setParameter);

        return q.getResultList();
    }
}
