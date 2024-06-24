package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.Car;
import vn.com.cardoctor.garage_service.models.dtos.CarDto;
import vn.com.cardoctor.garage_service.models.dtos.HistoryRepairOrderDto;
import vn.com.cardoctor.garage_service.repositories.CarRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class CarRepositoryImpl implements CarRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllCar(Long carBrandId, Long carModelId, Long carYearId, Long carVersionId, String licensePlate,
                                         Long garageId, Long customerTypeId, Long customerId, String filter, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select c.*, ctm.full_name as customer_name, ctm.phone_number as customer_phone, ct.customer_type_name, cccb.title as car_brand_name, cccm.title as car_model_name, cccy.title as car_year_name, ccce.title as car_version_name from cars c ");
        jpql.append(" left join customers ctm on c.customer_id = ctm.id ");
        jpql.append(" left join customer_types ct on ctm.customer_type_id = ct.id ");
        jpql.append(" left join `cardoctor_cms`.car_brands cccb on c.car_brand_id = cccb.id ");
        jpql.append(" left join `cardoctor_cms`.car_models cccm on c.car_model_id = cccm.id ");
        jpql.append(" left join `cardoctor_cms`.car_years cccy on c.car_year_id = cccy.id ");
        jpql.append(" left join `cardoctor_cms`.car_versions ccce on c.car_version_id = ccce.id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and lower(c.garage_id) like :garageId ");
        }
        if (Objects.nonNull(filter)) {
            params.put("filter", "%" + filter.toLowerCase() + "%");
            jpql.append("   and concat(c.license_plate, c.vin_number, ctm.full_name, ctm.phone_number) like :filter ");
        }
        if (Objects.nonNull(customerTypeId)) {
            params.put("customerTypeId", customerTypeId);
            jpql.append("   and ct.id = :customerTypeId ");
        }
        if (Objects.nonNull(carBrandId)) {
            params.put("carBrandId", carBrandId);
            jpql.append("   and c.car_brand_id = :carBrandId ");
        }
        if (Objects.nonNull(carModelId)) {
            params.put("carModelId", carModelId);
            jpql.append("   and c.car_model_id = :carModelId ");
        }
        if (Objects.nonNull(carYearId)) {
            params.put("carYearId", carYearId);
            jpql.append("   and c.car_year_id = :carYearId ");
        }
        if (Objects.nonNull(carVersionId)) {
            params.put("carVersionId", carVersionId);
            jpql.append("   and c.car_version_id = :carVersionId ");
        }
        if (Objects.nonNull(licensePlate)) {
            params.put("licensePlate", licensePlate.toLowerCase());
            jpql.append("   and lower(c.license_plate) = :licensePlate ");
        }
        if (Objects.nonNull(customerId)) {
            params.put("customerId", customerId);
            jpql.append("   and c.customer_id = :customerId ");
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" group by c.id ORDER BY c.created_at DESC LIMIT :limit OFFSET :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "CarDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public List<HistoryRepairOrderDto> findAllHistoryRepairOrder(Long carId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("SELECT cu.id AS customerId, cu.full_name AS customerName, r.id AS repairOrderId, " +
                "r.description AS repairOrderDescription, r.appointment_date AS appointmentDate, q.total_price AS totalPrice, ");
        jpql.append("(SELECT GROUP_CONCAT(p.name SEPARATOR ' - ') FROM quotations q2");
        jpql.append(" INNER JOIN quotation_infos qi ON q2.id = qi.quotation_id ");
        jpql.append(" INNER JOIN products p ON qi.product_id = p.id ");
        jpql.append(" WHERE q2.repair_order_id = r.id) AS products, ");
        jpql.append("(SELECT GROUP_CONCAT(gs.name SEPARATOR ' - ') FROM quotations q2");
        jpql.append(" INNER JOIN quotation_infos qi ON q2.id = qi.quotation_id ");
        jpql.append(" LEFT JOIN garage_services gs ON qi.product_id = gs.id ");
        jpql.append(" WHERE q2.repair_order_id = r.id) AS services ");
        jpql.append(" FROM repair_orders r");
        jpql.append(" INNER JOIN cars c ON r.car_id = c.id ");
        jpql.append(" INNER JOIN customers cu ON r.customer_id = cu.id ");
        jpql.append(" LEFT JOIN quotations q ON r.id = q.repair_order_id ");
        jpql.append(" WHERE 1 = 1 ");

        if (Objects.nonNull(carId)) {
            jpql.append(" AND c.id = :carId ");
            params.put("carId", carId);
        }

        jpql.append(" ORDER BY r.appointment_date DESC, r.id DESC");

        Query query = entityManager.createNativeQuery(jpql.toString());

        // Đặt tham số vào truy vấn
        params.forEach((key, value) -> query.setParameter(key, value));

        List<Object[]> results = query.getResultList();

        List<HistoryRepairOrderDto> historyRepairOrderDtos = new ArrayList<>();
        for (Object[] result : results) {
            HistoryRepairOrderDto dto = new HistoryRepairOrderDto();
            dto.setCustomerId(result[0] != null ? ((BigInteger) result[0]).longValue() : null);
            dto.setCustomerName(result[1] != null ? (String) result[1] : null);
            dto.setRepairOrderId(result[2] != null ? ((BigInteger) result[2]).longValue() : null);
            dto.setRepairOrderDescription(result[3] != null ? (String) result[3] : null);
            dto.setAppointmentDate(result[4] != null ? ((Date) result[4]) : null);
            dto.setTotalPrice(result[5] != null ? (BigDecimal) result[5] : null);
            dto.setProducts(result[6] != null ? (String) result[6] : null);
            dto.setServices(result[7] != null ? (String) result[7] : null);
            historyRepairOrderDtos.add(dto);
        }

        return historyRepairOrderDtos;
    }

    @Override
    public List<CarDto> findCarDtoByCustomerId(Long customerId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select c.*, null as customer_name, null as customer_type_name, null as customer_phone, cccb.title as car_brand_name, cccm.title as car_model_name, cccy.title as car_year_name, ccce.title as car_version_name from cars c ");
        jpql.append(" left join `cardoctor_cms`.car_brands cccb on c.car_brand_id = cccb.id ");
        jpql.append(" left join `cardoctor_cms`.car_models cccm on c.car_model_id = cccm.id ");
        jpql.append(" left join `cardoctor_cms`.car_years cccy on c.car_year_id = cccy.id ");
        jpql.append(" left join `cardoctor_cms`.car_versions ccce on c.car_version_id = ccce.id ");
        jpql.append(" where c.customer_id = :customerId ");
        params.put("customerId", customerId);
        Query q = entityManager.createNativeQuery(jpql.toString(), "CarDto");
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    @Override
    public Boolean checkExistCarByLicensePlateAndGarageId(String licensePlate, Long garageId, Long currentId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select c.* from cars c ");
        jpql.append("   where c.license_plate = :licensePlate ");
        jpql.append("   and c.garage_id = :garageId ");
        if (Objects.nonNull(currentId)) {
            jpql.append("   and c.id != :currentId ");
            params.put("currentId", currentId);
        }

        params.put("licensePlate", licensePlate);
        params.put("garageId", garageId);

        Query q = entityManager.createNativeQuery(jpql.toString(), Car.class);
        params.forEach(q::setParameter);
        List<Car> cars = (List<Car>) q.getResultList();
        if (cars.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkExistCarByVinNumberAndGarageId(String vinNumber, Long garageId, Long currentId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select c.* from cars c ");
        jpql.append("   where c.vin_number = :vinNumber ");
        jpql.append("   and c.garage_id = :garageId ");
        if (Objects.nonNull(currentId)) {
            jpql.append("   and c.id != :currentId ");
            params.put("currentId", currentId);
        }

        params.put("vinNumber", vinNumber);
        params.put("garageId", garageId);

        Query q = entityManager.createNativeQuery(jpql.toString(), Car.class);
        params.forEach(q::setParameter);
        List<Car> cars = (List<Car>) q.getResultList();
        if (cars.size() > 0) {
            return true;
        }
        return false;
    }
}
