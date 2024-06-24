package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.GarageGroup;
import vn.com.cardoctor.garage_service.repositories.garages.GarageGroupRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GarageGroupRepositoryImpl implements GarageGroupRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findGarageGroup(String name, String phone, String website, String email, String taxCode,
                                              Integer status, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select gg.* from garage_groups gg ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(name)) {
            jpql.append("   and gg.name like :name ");
            params.put("name", "%" + name + "%");
        }
        if (Objects.nonNull(phone)) {
            jpql.append("   and gg.phone like :phone ");
            params.put("phone", "%" + phone + "%");
        }
        if (Objects.nonNull(website)) {
            jpql.append("   and gg.website like :website ");
            params.put("website", "%" + website + "%");
        }
        if (Objects.nonNull(email)) {
            jpql.append("   and gg.email like :email ");
            params.put("email", "%" + email + "%");
        }
        if (Objects.nonNull(taxCode)) {
            jpql.append("   and gg.tax_code like :taxCode ");
            params.put("taxCode", "%" + taxCode + "%");
        }
        if (Objects.nonNull(status)) {
            jpql.append("   and gg.status = :status ");
            params.put("status", status);
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by gg.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), GarageGroup.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
