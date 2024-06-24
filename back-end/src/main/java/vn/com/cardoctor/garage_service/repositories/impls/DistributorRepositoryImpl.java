package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.enums.DeliverySparePartStatus;
import vn.com.cardoctor.garage_service.models.responses.dashboards.OutputOrderDistributor;
import vn.com.cardoctor.garage_service.repositories.DistributorRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.DataUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;

public class DistributorRepositoryImpl implements DistributorRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllDistributor(String code, String name, Long provinceId, Long districtId, Long wardId,
                                                 String address, Long garageId, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select d.*, a.name as province_name, b.name as district_name, c.name as ward_name, g.name as garage_name from distributors d ");
        jpql.append("   left join `cardoctor_cms`.area a on a.id = d.province_id ");
        jpql.append("   left join `cardoctor_cms`.area b on b.id = d.district_id ");
        jpql.append("   left join `cardoctor_cms`.area c on c.id = d.ward_id ");
        jpql.append("   left join garages g on g.id = d.garage_id ");
        jpql.append("   where 1 = 1 ");
        jpql.append("   and d.is_delete = 0 ");
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and d.garage_id = :garageId ");
        }
        if (Objects.nonNull(code)) {
            params.put("code", "%" + code + "%");
            jpql.append("   and lower(d.code) like :code ");
        }
        if (Objects.nonNull(name)) {
            params.put("name", "%" + name + "%");
            jpql.append("   and lower(d.name) like :name ");
        }
        if (Objects.nonNull(provinceId)) {
            params.put("provinceId", provinceId);
            jpql.append("   and d.province_id = :provinceId ");
        }
        if (Objects.nonNull(districtId)) {
            params.put("districtId", districtId);
            jpql.append("   and d.district_id = :districtId ");
        }
        if (Objects.nonNull(wardId)) {
            params.put("wardId", wardId);
            jpql.append("   and d.ward_id = :wardId ");
        }
        if (Objects.nonNull(address)) {
            params.put("address", "%" + address + "%");
            jpql.append("   and lower(d.address) like :address ");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by d.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "DistributorDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;


    }

    @Override
    public List<OutputOrderDistributor> findAllOutputOrderDistributors(Long garageId, Long quantityLabel) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" SELECT d.id, d.code, d.name, COALESCE(SUM(od.total_price), 0) AS total_output ");
        jpql.append(" FROM distributors d ");
        jpql.append(" LEFT JOIN orders_distributor od ON d.id = od.distributor_id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and d.garage_id = (:garageId) ");
        }
        jpql.append("   and od.delivery_status = " + DeliverySparePartStatus.RECEIVE_PRODUCT.getCode() + " ");
        jpql.append(" GROUP BY d.id ORDER BY total_output DESC ");
        if (Objects.nonNull(quantityLabel)) {
            params.put("quantityLabel", quantityLabel);
            jpql.append(" LIMIT :quantityLabel ");
        } else {
            jpql.append(" LIMIT 5 ");
        }
        StringBuilder countQueryStr = new StringBuilder();
        countQueryStr.append(" select count(*) from (").append(jpql).append(" ) count");
        Query countQuery = entityManager.createNativeQuery(countQueryStr.toString());
        Query selectQuery = entityManager.createNativeQuery(jpql.toString());
        if (!params.isEmpty()) {
            params.forEach(selectQuery::setParameter);
            params.forEach(countQuery::setParameter);
        }
        long total = ((BigInteger) countQuery.getSingleResult()).longValue();
        if (total > 0) {
            List<Object[]> rows = selectQuery.getResultList();
            List<OutputOrderDistributor> result = new ArrayList<>();
            for (Object[] row : rows) {
                OutputOrderDistributor outputOrderDistributor = new OutputOrderDistributor();
                outputOrderDistributor.setDistributorId(DataUtils.safeToLong(row[0]));
                outputOrderDistributor.setDistributorCode(DataUtils.safeToString(row[1]));
                outputOrderDistributor.setDistributorName(DataUtils.safeToString(row[2]));
                outputOrderDistributor.setOutput(DataUtils.safeToBigDecimal(row[3]));
                result.add(outputOrderDistributor);
            }
            return result;
        }
        return Collections.emptyList();
    }
}
