package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.EService;
import vn.com.cardoctor.garage_service.repositories.garages.EServiceRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EServiceRepositoryImpl implements EServiceRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PagingDataResponse getAllEService(String name, String description, Integer status, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select s.* from services s ");
        jpql.append(" where 1 = 1 ");

        if (Objects.nonNull(name)) {
            params.put("name", "%" + name + "%");
            jpql.append("and s.name like :name");
        }

        if (Objects.nonNull(description)) {
            params.put("description", "%" + description + "%");
            jpql.append("and s.description like :description");
        }

        if (Objects.nonNull(status)) {
            params.put("status", status);
            jpql.append("and s.status = :status)");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" ORDER BY s.created_at DESC LIMIT :limit OFFSET :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), EService.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
