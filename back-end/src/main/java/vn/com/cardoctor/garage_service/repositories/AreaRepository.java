package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.rest.Area;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AreaRepository {
    @PersistenceContext
    EntityManager entityManager;

    public PagingDataResponse findAreaDto(String provinceCode, String districtCode, String wardCode)
            throws ApiException {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT ward.id as id, ward.id as ward_id, district.id as district_id, province.id as province_id ");
        jpql.append(" From `cardoctor_cms`.area ward ");
        jpql.append(" JOIN `cardoctor_cms`.area district on district.id = ward.parent_id ");
        jpql.append(" JOIN `cardoctor_cms`.area province on province.id = district.parent_id ");
        jpql.append(" WHERE (province.code LIKE LOWER(:provinceCode)) ");
        jpql.append(" AND (district.code LIKE LOWER(:districtCode)) ");
        jpql.append(" AND (ward.code LIKE LOWER(:wardCode)) ");

        Map<String, Object> params = new HashMap<>();
        params.put("provinceCode", provinceCode);
        params.put("districtCode", districtCode);
        params.put("wardCode", wardCode);

        StringBuilder strSelectQuery = new StringBuilder();
        strSelectQuery.append(
                " SELECT ward.id as id, ward.id as ward_id, district.id as district_id, province.id as province_id ");
        strSelectQuery.append(jpql);

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        Query q = entityManager.createNativeQuery(jpql.toString(), Area.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setTotalElement(total);
        response.setData(q.getResultList());
        return response;
    }

    public Long findAreaByNameAndParentId(String name, Long parentId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" SELECT a.id FROM `cardoctor_cms`.area a ");
        jpql.append(" where a.name = :name ");
        jpql.append(" and a.status = 'ACCEPTED' ");
        if (Objects.nonNull(parentId)) {
            jpql.append(" and a.parent_id = :parentId ");
            params.put("parentId", parentId);
        }
        params.put("name", name);
        Query q = entityManager.createNativeQuery(jpql.toString());
        params.forEach(q::setParameter);
        BigInteger result = (BigInteger) q.getSingleResult();
        return result.longValue();
    }
}
