package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.Inventory;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.repositories.InventoryRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryRepositoryImpl implements InventoryRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllInventory(Long garageId, String name, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select i.* from inventories i ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            jpql.append("   and i.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(name)) {
            jpql.append("   and i.name like :name ");
            params.put("name", "%" + name + "%");
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by i.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), Inventory.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
