package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.Customer;
import vn.com.cardoctor.garage_service.repositories.CustomerRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllCustomer(Long garageId, Long customerTypeId, String customerName, String phoneNumber,
                                              Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select c.*, ct.customer_type_name from customers c ");
        jpql.append("   left join customer_types ct on c.customer_type_id = ct.id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and c.garage_id = :garageId");
        }
        if (Objects.nonNull(customerTypeId)) {
            params.put("customerTypeId", customerTypeId);
            jpql.append("   and c.customer_type_id = :customerTypeId ");
        }
        if (Objects.nonNull(customerName)) {
            params.put("customerName", "%" + customerName + "%");
            jpql.append("   and lower(c.full_name) like lower(:customerName) ");
        }
        if (Objects.nonNull(phoneNumber)) {
            params.put("phoneNumber", "%" + phoneNumber + "%");
            jpql.append("   and c.phone_number like :phoneNumber ");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" ORDER BY c.created_at DESC LIMIT :limit OFFSET :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "CustomerDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
