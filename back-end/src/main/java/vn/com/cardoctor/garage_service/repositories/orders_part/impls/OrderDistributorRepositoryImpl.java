package vn.com.cardoctor.garage_service.repositories.orders_part.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.repositories.orders_part.OrderDistributorRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderDistributorRepositoryImpl implements OrderDistributorRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findAllOrderDistributor(Long garageId, String orderCode, Long distributorId, String distributorCode, String distributorName, Date fromDate, Date toDate,
                                                      Integer deliveryStatus, Integer paymentStatus, Integer pageSize, Integer pageNumber) {

        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append("select od.*, d.name as distributor_name from orders_distributor od ");
        jpql.append("   join distributors d on od.distributor_id = d.id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            jpql.append("   and od.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(orderCode)) {
            jpql.append("   and lower(concat_ws('-', od.order_code, d.name)) like :orderCode ");
            params.put("orderCode", "%" + orderCode + "%");
        }
        if (Objects.nonNull(distributorId)) {
            jpql.append("   and d.id = :distributorId ");
            params.put("distributorId", distributorId);
        }
        if (Objects.nonNull(distributorCode)) {
            jpql.append("   and lower(d.code) like :distributorCode ");
            params.put("distributorCode", "%" + distributorCode + "%");
        }
        if (Objects.nonNull(distributorName)) {
            jpql.append("   and d.name like :distributorName ");
            params.put("distributorName", "%" + distributorName + "%");
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
        jpql.append("   and  date_format(od.created_at, '%Y-%m-%d') between :fromDate and :toDate ");
        if (Objects.nonNull(deliveryStatus)) {
            jpql.append("   and od.delivery_status = :deliveryStatus ");
            params.put("deliveryStatus", deliveryStatus);
        }
        if (Objects.nonNull(paymentStatus)) {
            jpql.append("   and od.payment_status = :paymentStatus ");
            params.put("paymentStatus", paymentStatus);
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by od.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "OrderDistributorDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
