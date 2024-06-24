package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.Employee;
import vn.com.cardoctor.garage_service.repositories.garages.EmployeeRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PagingDataResponse searchEmployee(Long garageId, String fullName, Integer type, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        jpql.append("select e.* from employees e where 1 = 1 ");

        if (fullName != null) {
            jpql.append(" and (lower(e.full_name) like lower(:fullName)) ");
            params.put("fullName", "%" + fullName.toLowerCase() + "%");
        }
        if (garageId != null) {
            jpql.append("   and e.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (type != null) {
            jpql.append(" and e.contract_type = :type ");
            params.put("type", type);
        }
        if (status != null) {
            jpql.append(" and e.status = :status ");
            params.put("status", status);
        }
//        jpql.append(" and ((e.start_date between :startDate and :endDate) or (e.end_date between :startDate and :endDate) ) ");
        if (Objects.nonNull(startDate)) {
            jpql.append(" and e.start_date = :startDate ");
            params.put("startDate", format.format(startDate));
        }
        if (Objects.nonNull(endDate)) {
            jpql.append(" and e.end_date = :endDate ");
            params.put("endDate", format.format(endDate));
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by e.created_at desc limit :limit offset :offset ");
        page = page != null ? page - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (page >= 0 ? page : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), Employee.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(page + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public Boolean isDuplicateName(String name, Long employeeId, Long garageId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select e.* from employees e ");
        jpql.append("   where e.full_name = :name ");
        jpql.append("   and e.garage_id = :garageId ");
        if (Objects.nonNull(employeeId)) {
            jpql.append("   and e.id != :employeeId ");
            params.put("employeeId", employeeId);
        }

        params.put("name", name);
        params.put("garageId", garageId);

        Query q = entityManager.createNativeQuery(jpql.toString(), Employee.class);
        params.forEach(q::setParameter);
        List<Employee> employees = (List<Employee>) q.getResultList();
        if (employees.size() > 0) {
            return true;
        }
        return false;
    }
}
