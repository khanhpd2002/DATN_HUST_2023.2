package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.repositories.garages.GarageContractRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GarageContractRepositoryImpl implements GarageContractRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllGarageContract(String garageName, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select gc.*, g.name as garage_name from garage_contracts gc ");
        jpql.append("   left join garages g on g.id = gc.garage_id ");

        if (Objects.nonNull(garageName)) {
            jpql.append("       and g.name like :garageName");
            params.put("garageName", garageName);
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" ORDER BY gc.created_at DESC LIMIT :limit OFFSET :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "GarageContractDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
