package vn.com.cardoctor.garage_service.repositories.impls;

import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.enums.DeliverySparePartStatus;
import vn.com.cardoctor.garage_service.enums.InOutboundStatus;
import vn.com.cardoctor.garage_service.enums.OutboundProductStatus;
import vn.com.cardoctor.garage_service.models.dtos.ProductRefundedDto;
import vn.com.cardoctor.garage_service.models.responses.dashboards.CountProductResponse;
import vn.com.cardoctor.garage_service.repositories.DashboardRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class DashboardRepositoryImpl implements DashboardRepository {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public CountProductResponse getTotalProduct(Long garageId, Date fromDate, Date toDate) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select count(distinct(p.code)), sum(p.quantity) from products p ");
        jpql.append(" where p.created_at between :fromDate and date_add(:toDate, interval 1 day) ");
        jpql.append(" and p.garage_id = :garageId ");
        params.put("garageId", garageId);
        setParamsDate(params, fromDate, toDate);
        Query query = entityManager.createNativeQuery(jpql.toString());
        params.forEach(query::setParameter);
        Object[] res = (Object[]) query.getSingleResult();
        CountProductResponse response = new CountProductResponse();
        response.setTotalProductCode(((BigInteger) res[0]).longValue());
        response.setTotalProductQuantity(res[1] != null ? ((BigDecimal) res[1]).longValue() : 0);
        return response;
    }

    @Override
    public BigDecimal getInventoryValue(Long garageId, Date fromDate, Date toDate) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select sum(p.quantity * p.purchase_price) from products p ");
        jpql.append(" where p.created_at between :fromDate and :toDate ");
        jpql.append(" and p.parent_product_id is null ");
        jpql.append(" and p.garage_id = :garageId ");
        params.put("garageId", garageId);
        setParamsDate(params, fromDate, toDate);
        Query query = entityManager.createNativeQuery(jpql.toString());
        params.forEach(query::setParameter);
        BigDecimal res = (BigDecimal) query.getSingleResult();
        return res;
    }

    @Override
    public Long getTotalTicketHasNotDelivery(Long garageId, Date fromDate, Date toDate) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select count(od.id) from orders_distributor od ");
        jpql.append(" where od.created_at between :fromDate and :toDate ");
        jpql.append(" and od.delivery_status in (:deliveriedStatus) ");
        jpql.append(" and od.garage_id = :garageId ");
        params.put("garageId", garageId);
        List<Integer> deliveriedStatus = new ArrayList<>();
        deliveriedStatus.add(DeliverySparePartStatus.ON_THE_WAY.getCode());
        deliveriedStatus.add(DeliverySparePartStatus.RECEIVE_ORDER.getCode());
        params.put("deliveriedStatus", deliveriedStatus);
        setParamsDate(params, fromDate, toDate);
        Query query = entityManager.createNativeQuery(jpql.toString());
        params.forEach(query::setParameter);
        BigInteger res = (BigInteger) query.getSingleResult();
        return res.longValue();
    }

    @Override
    public List<ProductRefundedDto> getMostProductRefunded(Long garageId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select p.code, p.name, sum(op.export_quantity) as refund_quantity from outbound_products op ");
        jpql.append(" join products p on op.product_id = p.id ");
        jpql.append(" join outbound_tickets ot on op.outbound_ticket_id = ot.id and ot.type = :ticketStatus ");
        jpql.append(" where op.status = :productStatus and ot.garage_id = :garageId ");
        jpql.append(" group by op.product_id order by refund_quantity desc limit 5 ");
        params.put("ticketStatus", InOutboundStatus.VERIFIED.getCode());
        params.put("productStatus", OutboundProductStatus.DA_XUAT.getCode());
        params.put("garageId", garageId);

        Query selectQuery = entityManager.createNativeQuery(jpql.toString(), "ProductRefundedDto");
        params.forEach(selectQuery::setParameter);
        return selectQuery.getResultList();
    }

    private void setParamsDate(Map<String, Object> params, Date fromDate, Date toDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (Objects.isNull(fromDate)) {
            params.put("fromDate", "0000-00-00");
        } else {
            params.put("fromDate", format.format(fromDate));
        }
        if (Objects.isNull(toDate)) {
            params.put("toDate", "9999-12-12");
        } else {
            params.put("toDate", format.format(toDate));
        }
    }
}
