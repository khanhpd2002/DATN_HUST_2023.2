package vn.com.cardoctor.garage_service.repositories.in_out_bounds.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.InboundTicket;
import vn.com.cardoctor.garage_service.entities.OutboundTicket;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InboundTicketRepositoryImpl implements InboundTicketRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllInboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                                    Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append(" select it.* from inbound_tickets it ");
        jpql.append(" where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            jpql.append(" and it.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(type)) {
            jpql.append(" and it.type = :type ");
            params.put("type", type);
        }
        if (!StringUtil.isNullOrEmpty(code)) {
            jpql.append(" and it.code = :code ");
            params.put("code", code);
        }
        if (Objects.nonNull(status)) {
            jpql.append(" and it.status = :status ");
            params.put("status", status);
        }
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
        jpql.append(" and it.created_at between :fromDate and date_add(:toDate, interval 1 day) ");

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" group by it.id order by it.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), InboundTicket.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public BigDecimal getNumberTicketRefundRate(Long garageId, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select count(distinct it.ticket_id) from inbound_tickets it ");
        sb.append("where 1 = 1 ");

        if (Objects.nonNull(garageId)) {
            sb.append("and garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and type = 2 ");

        sb.append(" and DATE(created_at) between :fromDate and :toDate ");

        if (Objects.isNull(fromDate)) {
            params.put("fromDate", "0000-00-00");
        } else {
            params.put("fromDate", fromDate);
        }

        if (Objects.isNull(toDate)) {
            params.put("toDate", "9999-12-12");
        } else {
            params.put("toDate", toDate);
        }

        Query countQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(countQuery::setParameter);
        long total = ((BigInteger) countQuery.getSingleResult()).longValue();
        return new BigDecimal(total);
    }
}
