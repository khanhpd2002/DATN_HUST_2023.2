package vn.com.cardoctor.garage_service.repositories.garages.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.Car;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.TextUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GarageServiceRepositoryImpl implements GarageServiceRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findAll(Long garageId, String name, String code, Long garageServiceTypeId, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select gs.*, gst.name as garage_service_type from garage_services gs ");
        jpql.append(" join garage_service_types gst on gs.garage_service_type_id = gst.id ");
        jpql.append(" where ( gs.garage_id = :garageId or gs.garage_id is null ) ");
        jpql.append(" and gs.is_active = 1 ");
        params.put("garageId", garageId);

        if (!TextUtil.isNullOrEmpty(name)) {
            jpql.append(" and gs.name like :name ");
            params.put("name", "%" + name + "%");
        }
        if (!TextUtil.isNullOrEmpty(code)) {
            jpql.append(" and gs.code like :code ");
            params.put("code", "%" + code + "%");
        }
        if (Objects.nonNull(garageServiceTypeId)) {
            jpql.append(" and gs.garage_service_type_id = :garageServiceTypeId ");
            params.put("garageServiceTypeId", garageServiceTypeId);
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by gs.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "GarageServiceDto");
        params.forEach(q::setParameter);
        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public PagingDataResponse getServiceApplyForAllGarage(String name, String code, Long garageServiceTypeId, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select gs.*, gst.name as garage_service_type from garage_services gs ")
            .append(" join garage_service_types gst on gs.garage_service_type_id = gst.id ")
            .append(" where gs.garage_id is null ")
            .append(" and gs.is_active = 1 ");

        if (!TextUtil.isNullOrEmpty(name)) {
            jpql.append(" and gs.name like :name ");
            params.put("name", "%" + name + "%");
        }
        if (!TextUtil.isNullOrEmpty(code)) {
            jpql.append(" and gs.code like :code ");
            params.put("code", "%" + code + "%");
        }
        if (Objects.nonNull(garageServiceTypeId)) {
            jpql.append(" and gs.garage_service_type_id = :garageServiceTypeId ");
            params.put("garageServiceTypeId", garageServiceTypeId);
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by gs.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "GarageServiceDto");
        params.forEach(q::setParameter);
        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public Boolean checkExistGarageServiceByCodeAndGarageId(String code, Long garageId, Long currentId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select gs.* from garage_services gs ");
        jpql.append(" where gs.garage_id = :garageId ");
        jpql.append(" and gs.code = :code ");
        if (Objects.nonNull(currentId)) {
            jpql.append(" and gs.id != :currentId ");
            params.put("currentId", currentId);
        }
        params.put("garageId", garageId);
        params.put("code", code);

        Query q = entityManager.createNativeQuery(jpql.toString(), GarageService.class);
        params.forEach(q::setParameter);
        List<GarageService> garageServices = (List<GarageService>) q.getResultList();
        if (garageServices.size() > 0) {
            return true;
        }
        return false;
    }
}
