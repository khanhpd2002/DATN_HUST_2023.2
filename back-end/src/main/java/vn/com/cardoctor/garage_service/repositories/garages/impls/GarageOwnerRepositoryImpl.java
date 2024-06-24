package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;
import vn.com.cardoctor.garage_service.repositories.garages.GarageOwnerRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GarageOwnerRepositoryImpl implements GarageOwnerRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllGarageOwner(String name, String userName, String phone, String email, Integer gender,
                                                 Date fromDateBirthday, Date toDateBirthday, Integer status, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append("select * from garage_owners go ");
        jpql.append("where 1 = 1 ");

        if (Objects.nonNull(name)) {
            params.put("name", "%" + name + "%");
            jpql.append("and go.name like :name ");
        }
        if (Objects.nonNull(userName)) {
            params.put("userName", "%" + userName + "%");
            jpql.append("and go.user_name like :userName ");
        }
        if (Objects.nonNull(phone)) {
            params.put("phone", "%" + phone + "%");
            jpql.append("and go.phone like :phone ");
        }
        if (Objects.nonNull(email)) {
            params.put("email", "%" + email + "%");
            jpql.append("and go.email like :email ");
        }
        if (Objects.nonNull(gender)) {
            params.put("gender", gender);
            jpql.append("and go.gender = :gender ");
        }
        if (Objects.nonNull(status)) {
            params.put("status", status);
            jpql.append("and go.status = :status ");
        }
        if (Objects.isNull(fromDateBirthday)) {
            params.put("fromDate", "0000-00-00");
        } else {
            params.put("fromDate", format.format(fromDateBirthday));
        }
        if (Objects.isNull(toDateBirthday)) {
            params.put("toDate", "9999-12-12");
        } else {
            params.put("toDate", format.format(toDateBirthday));
        }
        jpql.append("   and  date_format(go.created_at, '%Y-%m-%d') between :fromDate and :toDate ");

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by go.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), GarageOwner.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }
}
