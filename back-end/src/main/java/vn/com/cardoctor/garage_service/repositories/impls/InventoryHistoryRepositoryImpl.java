package vn.com.cardoctor.garage_service.repositories.impls;

import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.InventoryHistory;
import vn.com.cardoctor.garage_service.entities.InventoryHistoryDetail;
import vn.com.cardoctor.garage_service.repositories.InventoryHistoryRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Log4j2
public class InventoryHistoryRepositoryImpl implements InventoryHistoryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findAllInventoryHistory(Long inventoryId, Date fromDate, Date toDate, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append("select ih.* from inventory_histories ih ");
        jpql.append("   where ih.count_date between :fromDate and :toDate ");
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
        log.info("============= fromDate is {}, toDate is {} ============", params.get("fromDate"), params.get("toDate"));
        if (Objects.nonNull(inventoryId)) {
            params.put("inventoryId", inventoryId);
            jpql.append("   and ih.inventory_id = :inventoryId ");
        }
        jpql.append(" group by ih.id order by ih.created_at desc ");

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), InventoryHistory.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public PagingDataResponse getDetailHistory(Long historyId, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select ihd.* from inventory_histories_detail ihd ");
        jpql.append("   where ihd.inventory_history_id = :historyId ");
        params.put("historyId", historyId);

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by ihd.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), InventoryHistoryDetail.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
