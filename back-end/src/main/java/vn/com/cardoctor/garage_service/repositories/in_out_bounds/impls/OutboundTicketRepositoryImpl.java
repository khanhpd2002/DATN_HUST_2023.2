package vn.com.cardoctor.garage_service.repositories.in_out_bounds.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.OutboundTicket;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OutboundTicketRepositoryImpl implements OutboundTicketRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllOutboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                                    Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append(" select ot.* from outbound_tickets ot ");
        jpql.append(" where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            jpql.append(" and ot.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(type)) {
            jpql.append(" and ot.type = :type ");
            params.put("type", type);
        }
        if (!StringUtil.isNullOrEmpty(code)) {
            jpql.append(" and ot.code = :code ");
            params.put("code", code);
        }
        if (Objects.nonNull(status)) {
            jpql.append(" and ot.status = :status ");
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
        jpql.append(" and ot.created_at between :fromDate and date_add(:toDate, interval 1 day) ");

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" group by ot.id order by ot.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), OutboundTicket.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
