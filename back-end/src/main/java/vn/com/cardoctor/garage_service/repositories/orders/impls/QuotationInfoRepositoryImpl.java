package vn.com.cardoctor.garage_service.repositories.orders.impls;

import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;
import vn.com.cardoctor.garage_service.repositories.orders.QuotationInfoRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuotationInfoRepositoryImpl implements QuotationInfoRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuotationProductDto> findAllQuotationInfo(Long quotationId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select qi.*, qi.type as product_type, gs.garage_service_type_id, p.code as product_code, p.name as product_name, p.unit, e.full_name as employee_name, gst.name as garage_service_type_name from quotation_infos qi ");
        jpql.append("   left join products p on qi.product_id = p.id ");
        jpql.append("   left join garage_services gs on qi.garage_service_id = gs.id ");
        jpql.append("   left join garage_service_types gst on gst.id = gs.garage_service_type_id ");
        jpql.append("   left join employees e on qi.employee_id = e.id ");
        jpql.append("   where qi.quotation_id = :quotationId ");

        params.put("quotationId", quotationId);
        Query q = entityManager.createNativeQuery(jpql.toString(), "QuotationProductDto");
        params.forEach(q::setParameter);

        return q.getResultList();
    }
}

