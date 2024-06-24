package vn.com.cardoctor.garage_service.repositories.orders.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.enums.RepairOrderStatus;
import vn.com.cardoctor.garage_service.models.dtos.RevenueRepairDto;
import vn.com.cardoctor.garage_service.models.requests.repair_order.FilterRepairOrder;
import vn.com.cardoctor.garage_service.models.responses.DashboardResponse;
import vn.com.cardoctor.garage_service.models.responses.TypeServicePopularResponse;
import vn.com.cardoctor.garage_service.repositories.orders.RepairOrderRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings({"unchecked"})
public class RepairOrderRepositoryImpl implements RepairOrderRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String FROM_DATE = "fromDate";
    private static final String TO_DATE = "toDate";

    @Override
    public PagingDataResponse getAllRepairOrder(FilterRepairOrder filterRepairOrder) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select ro.*, ");
        jpql.append("c.full_name as customer_name, ct.customer_type_name, q.total_price, g.name as garage_name, car.license_plate ");
        jpql.append("from repair_orders ro ");
        jpql.append("   join customers c on c.id = ro.customer_id ");
        jpql.append("   join customer_types ct on c.customer_type_id = ct.id ");
        jpql.append("   left join quotations q on q.repair_order_id = ro.id ");
        jpql.append("   left join garages g on g.id = ro.garage_id ");
        jpql.append("   left join cars car on car.id = ro.car_id ");
        jpql.append("   left join `cardoctor_cms`.car_brands cb on car.car_brand_id = cb.id ");
        jpql.append("   left join `cardoctor_cms`.car_models cm on car.car_model_id = cm.id ");
        jpql.append("   left join `cardoctor_cms`.car_years cy on car.car_year_id = cy.id ");
        jpql.append("   left join `cardoctor_cms`.car_versions cv on car.car_version_id = cv.id ");

        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(filterRepairOrder.getStatus())) {
            jpql.append("   and ro.status = :status ");
            params.put("status", filterRepairOrder.getStatus());
        }
        if (Objects.nonNull(filterRepairOrder.getPaymentStatus())) {
            jpql.append("   and ro.payment_status = :paymentStatus ");
            params.put("paymentStatus", filterRepairOrder.getPaymentStatus());
        }
        if (Objects.nonNull(filterRepairOrder.getCustomerTypeId())) {
            jpql.append("   and ct.id = :customerTypeId ");
            params.put("customerTypeId", filterRepairOrder.getCustomerTypeId());
        }
        if (Objects.nonNull(filterRepairOrder.getCustomerName())) {
            jpql.append("   and lower(c.full_name) like :fullName ");
            params.put("fullName", "%" + filterRepairOrder.getCustomerName().toLowerCase()+ "%");
        }
        if (Objects.nonNull(filterRepairOrder.getGarageId())) {
            jpql.append("   and ro.garage_id = :garageId ");
            params.put("garageId", filterRepairOrder.getGarageId());
        }
        if (Objects.nonNull(filterRepairOrder.getFilter())) {
            jpql.append("   and lower(concat_ws('-', ro.code, c.full_name, c.phone_number, car.car_name, car.license_plate, car.vin_number, cb.title, cm.title, cy.title, cv.title)) like :filter ");
            params.put("filter", "%" + filterRepairOrder.getFilter().toLowerCase() + "%");
        }
        this.setParamsDate(params, filterRepairOrder.getFromDate(), filterRepairOrder.getToDate());
        jpql.append("   and ro.appointment_date between :fromDate and :toDate ");

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by ro.created_at desc limit :limit offset :offset ");
        filterRepairOrder.setPageNumber(filterRepairOrder.getPageNumber() != null ? filterRepairOrder.getPageNumber() - 1 : 0);
        int limit = filterRepairOrder.getPageSize() != null ? filterRepairOrder.getPageSize() : 20;
        int offset = (filterRepairOrder.getPageNumber() >= 0 ? filterRepairOrder.getPageNumber() : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "RepairOrderDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(filterRepairOrder.getPageNumber() + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public List<Long> getExpertIdsInTicket() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select ro.expert_id from repair_orders ro ");
        jpql.append("   where ro.expert_id is not null ");
        jpql.append("   and ro.status not in (4, 5) ");
        jpql.append("       group by ro.expert_id ");

        Query nativeQuery = this.entityManager.createNativeQuery(jpql.toString());
        List<Long> expertIds = new ArrayList<>();
        for (int i = 0; i < nativeQuery.getResultList().size(); i++) {
            expertIds.add(Long.parseLong(nativeQuery.getResultList().get(i).toString()));
        }
        return expertIds;
    }

    @Override
    public Long getExpertIdMostFreeInTicket(List<Long> expertIdsCms) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select ro.expert_id from repair_orders ro ");
        jpql.append("   where ro.expert_id is not null ");
        jpql.append("   and ro.status not in (4, 5) ");
        jpql.append("   and ro.expert_id in :expertIdsCms ");
        jpql.append("       group by ro.expert_id ");
        jpql.append("       order by count(*) limit 1 ");

        params.put("expertIdsCms", expertIdsCms);

        Query nativeQuery = this.entityManager.createNativeQuery(jpql.toString());
        params.forEach(nativeQuery::setParameter);
        Optional<?> optional = nativeQuery.getResultList().stream().findFirst();
        Long expertId = null;
        if (optional.isPresent()) {
            expertId = Long.parseLong(String.valueOf(optional.get()));
        }
        return expertId;
    }

    @Override
    public List<Long> getOperatorIdsInTicket() {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select ro.operator_id from repair_orders ro ");
        jpql.append("   where ro.operator_id is not null ");
        jpql.append("   and ro.status not in (5, 6) ");
        jpql.append("       group by ro.operator_id ");

        Query nativeQuery = this.entityManager.createNativeQuery(jpql.toString());
        List<Long> operatorIds = new ArrayList<>();
        for (int i = 0; i < nativeQuery.getResultList().size(); i++) {
            operatorIds.add(Long.parseLong(nativeQuery.getResultList().get(i).toString()));
        }
        return operatorIds;
    }

    @Override
    public Long getOperatorIdMostFreeInTicket(List<Long> operatorIdsCms) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select ro.operator_id from repair_orders ro ");
        jpql.append("   where ro.operator_id is not null ");
        jpql.append("   and ro.status not in (4, 5) ");
        jpql.append("   and ro.operator_id in :operatorIdsCms ");
        jpql.append("       group by ro.operator_id ");
        jpql.append("       order by count(*) limit 1 ");

        params.put("operatorIdsCms", operatorIdsCms);

        Query nativeQuery = this.entityManager.createNativeQuery(jpql.toString());
        params.forEach(nativeQuery::setParameter);
        Optional<?> optional = nativeQuery.getResultList().stream().findFirst();
        Long operatorId = null;
        if (optional.isPresent()) {
            operatorId = Long.parseLong(String.valueOf(optional.get()));
        }
        return operatorId;
    }

    @Override
    public BigDecimal getRevenue(Long garageId, List<Integer> statusCancelRepairOrder, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sb.append("select COALESCE(sum(q.total_price),0) from repair_orders as ro ");
        sb.append("inner join quotations as q on ro.id = q.repair_order_id ");
        sb.append("where (1 = 1) ");

        if (Objects.nonNull(garageId)) {
            sb.append("and ro.garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        if (Objects.nonNull(statusCancelRepairOrder)) {
            sb.append("and ro.status not in (:status)");
            params.put("status", statusCancelRepairOrder);
        }

        sb.append(" and DATE(ro.created_at) between :fromDate and :toDate ");

        this.setParamsDate(params, fromDate, toDate);
        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);
        return (BigDecimal) selectQuery.getSingleResult();
    }

    @Override
    public BigDecimal getNumberServiceByStatus(Long garageId, Integer status, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append(" SELECT COUNT(DISTINCT qi.garage_service_id) AS unique_service_count ");
        sb.append(" from repair_orders ro ");
        sb.append(" inner join quotations q on ro.id = q.repair_order_id ");
        sb.append(" inner join quotation_infos qi on qi.quotation_id = q.id ");
        sb.append(" where (1 = 1)");
        if (Objects.nonNull(garageId)) {
            sb.append(" and garage_id = :garageId");
            params.put("garageId", garageId);
        }

        if (Objects.nonNull(status)) {
            sb.append(" and ro.status = :status ");
            params.put("status", status);
        }

        sb.append(" and DATE(ro.created_at) between :fromDate and :toDate ");

        this.setParamsDate(params, fromDate, toDate);

        Query countQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(countQuery::setParameter);
        long total = ((BigInteger) countQuery.getSingleResult()).longValue();
        return new BigDecimal(total);
    }

    @Override
    public List<DashboardResponse.PopularService> getListPopularServices(Long garageId) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("select gst.name, sum(qi.price) as price_orders from repair_orders ro ");
        sb.append("inner join quotations q on ro.id = q.repair_order_id ");
        sb.append("inner join quotation_infos qi on q.id = qi.quotation_id ");
        sb.append("inner join garage_services gs on qi.garage_service_id = gs.id ");
        sb.append("inner join garage_service_types gst on gs.garage_service_type_id = gst.id ");
        sb.append("where (1 = 1) ");
        sb.append("and qi.garage_service_id is not null ");

        if (Objects.nonNull(garageId)) {
            sb.append("and ro.garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and ro.status = :status ");
        params.put("status", RepairOrderStatus.DONE.getCode());

        sb.append("group by gst.id order by price_orders desc limit 5");

        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);

        List<Object[]> rowResults = selectQuery.getResultList();
        List<DashboardResponse.PopularService> results = new ArrayList<>();

        for (Object[] row : rowResults) {
            DashboardResponse.PopularService response = new DashboardResponse.PopularService();
            response.setName((String) row[0]);
            response.setMoney((BigDecimal) row[1]);
            results.add(response);
        }

        return results;
    }

    @Override
    public List<DashboardResponse.ExcellentStaff> getListExcellentStaffs(Long garageId, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("select e.full_name, e.type, sum(qi.price) as total_money from repair_orders ro ");
        sb.append("inner join quotations q on ro.id = q.repair_order_id ");
        sb.append("inner join quotation_infos qi on q.id = qi.quotation_id ");
        sb.append("inner join employees e on qi.employee_id = e.id ");
        sb.append("where 1 = 1 ");

        if (Objects.nonNull(garageId)) {
            sb.append("and ro.garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and ro.status = :status ");
        params.put("status", RepairOrderStatus.DONE.getCode());

        sb.append("and DATE(qi.created_at) between :fromDate and :toDate ");
        this.setParamsDate(params, fromDate, toDate);

        sb.append("group by e.id having total_money > 0 order by total_money desc limit 5");

        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);

        List<Object[]> rowResults = selectQuery.getResultList();
        List<DashboardResponse.ExcellentStaff> results = new ArrayList<>();
        for (Object[] row : rowResults) {
            DashboardResponse.ExcellentStaff response = new DashboardResponse.ExcellentStaff();
            response.setName((String) row[0]);
            response.setJobTitle((String) row[1]);
            response.setMoney((BigDecimal) row[2]);
            results.add(response);
        }

        return results;
    }

    public List<TypeServicePopularResponse.ListTypeServicePopularResponse> getListTypeServicePopular(Long garageId, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("select gst.id, gst.name, count(gst.id) as quantity from repair_orders ro ");
        sb.append("inner join quotations q on ro.id = q.repair_order_id ");
        sb.append("inner join quotation_infos qi on q.id = qi.quotation_id ");
        sb.append("inner join garage_services gs on qi.garage_service_id = gs.id ");
        sb.append("inner join garage_service_types gst on gs.garage_service_type_id = gst.id ");
        sb.append("where 1 = 1 ");

        sb.append("and ro.status = :status ");
        params.put("status", RepairOrderStatus.DONE.getCode());

        if (Objects.nonNull(garageId)) {
            sb.append("and ro.garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and DATE(ro.created_at) between :fromDate and :toDate ");
        this.setParamsDate(params, fromDate, toDate);

        sb.append("group by gst.id having quantity > 0 order by quantity desc");

        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);

        List<TypeServicePopularResponse.ListTypeServicePopularResponse> results = new ArrayList<>();
        List<Object[]> rawResults = selectQuery.getResultList();

        for (Object[] row : rawResults) {
            TypeServicePopularResponse.ListTypeServicePopularResponse response = new TypeServicePopularResponse.ListTypeServicePopularResponse();
            response.setId((BigInteger) row[0]);
            response.setName((String) row[1]);
            response.setQuantity((BigInteger) row[2]);
            results.add(response);
        }

        return results;
    }

    @Override
    public BigDecimal getNumberEmployeeOnRevenue(Long garageId, List<Integer> statusCancelRepairOrder, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("select count(distinct qi.employee_id) from repair_orders ro ");
        sb.append("inner join quotations q on ro.id = q.repair_order_id ");
        sb.append("inner join quotation_infos qi on q.id = qi.quotation_id ");
        sb.append("inner join employees e on qi.employee_id = e.id ");
        sb.append("where 1 = 1 ");

        sb.append("and ro.status not in (:status) ");
        params.put("status", statusCancelRepairOrder);

        sb.append("and qi.employee_id > 0 and e.status = :employeeStatus ");
        params.put("employeeStatus", 0);

        if (Objects.nonNull(garageId)) {
            sb.append("and ro.garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and DATE(qi.created_at) between :fromDate and :toDate ");
        this.setParamsDate(params, fromDate, toDate);

        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);

        long total = ((BigInteger) selectQuery.getSingleResult()).longValue();
        return new BigDecimal(total);
    }

    @Override
    public List<RevenueRepairDto> findRevenueOfRepair(String groupByType, String fromDate, String toDate, Long garageId) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        String select = "";
        String groupBy = "";
        String orderBy = "";
        if (groupByType.equals("week")) {
            select = " select sum(q.total_price) as revenue_of_repair, date(ro.created_at) as date, max(month(ro.created_at)) as string_date from repair_orders ro\n" +
                    "    join quotations q on ro.id = q.repair_order_id ";
            groupBy = " GROUP BY date";
        } else if (groupByType.equals("month")) {
            select = " select sum(q.total_price) as revenue_of_repair, max(date(ro.created_at)) as date, DATE_FORMAT(ro.created_at,'%v-%x') AS string_date from repair_orders ro\n" +
                    "    join quotations q on ro.id = q.repair_order_id ";
            groupBy = " GROUP BY string_date";
            orderBy = " ORDER BY date";
        } else if (groupByType.equals("year")) {
            select = " select sum(q.total_price) as revenue_of_repair, max(date(ro.created_at)) as date, DATE_FORMAT(ro.created_at,'%m-%Y') AS string_date from repair_orders ro\n" +
                    "    join quotations q on ro.id = q.repair_order_id ";
            groupBy = " GROUP BY string_date";
            orderBy = " ORDER BY date";
        }
        strQuery.append(select);
        strQuery.append(" WHERE :fromDate <= date(ro.created_at) and date(ro.created_at) <= date_add(:toDate, interval 1 day ) ");
        strQuery.append(" and ro.garage_id = :garageId ");
        strQuery.append(groupBy);
        strQuery.append(orderBy);

        this.setParamsDate(params, fromDate, toDate);
        params.put("garageId", garageId);

        Query q = entityManager.createNativeQuery(strQuery.toString());
        params.forEach(q::setParameter);
        List<Object[]> rawResults = q.getResultList();
        List<RevenueRepairDto> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            RevenueRepairDto revenueRepairDto = new RevenueRepairDto();
            revenueRepairDto.setRevenueOfRepair((BigDecimal) row[0]);
            revenueRepairDto.setDate((Date) row[1]);
            revenueRepairDto.setStringDate(row[2].toString());
            results.add(revenueRepairDto);
        }
        return results;
    }

    @Override
    public List<RevenueRepairDto> findRevenueOfRepairV2(String groupByType, Long garageId) {
        StringBuilder strQuery = new StringBuilder();

        if (groupByType.equals("week")) {
            strQuery.append("WITH RECURSIVE date_series AS (\n" +
                    "  SELECT DATE_ADD(DATE(NOW()), INTERVAL - 6 DAY) AS date_value\n" +
                    "  UNION ALL\n" +
                    "  SELECT DATE_ADD(date_value, INTERVAL 1 DAY)\n" +
                    "  FROM date_series\n" +
                    "  WHERE date_value <= DATE_ADD(DATE(NOW()), INTERVAL - 1 DAY)\n" +
                    ")\n" +
                    "select \n" +
                    "COALESCE(sum(q.total_price), 0) as revenue_of_repair,\n" +
                    "ds.date_value AS date\n" +
                    "FROM\n" +
                    "    date_series ds\n" +
                    "LEFT JOIN\n" +
                    "    repair_orders ro ON ds.date_value = DATE(ro.created_at) AND ro.garage_id = " + garageId + "\n" +
                    "LEFT JOIN quotations q on ro.id = q.repair_order_id\n" +
                    "GROUP BY\n" +
                    "    ds.date_value");
        }
        if (groupByType.equals("month")) {
            strQuery = queryCommon(groupByType, 4L, garageId);
        }
        if (groupByType.equals("year")) {
            strQuery = queryCommon(groupByType, 12L, garageId);
        }

        Query q = entityManager.createNativeQuery(strQuery.toString());
        List<Object[]> rawResults = q.getResultList();
        List<RevenueRepairDto> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            RevenueRepairDto revenueRepairDto = new RevenueRepairDto();

            revenueRepairDto.setRevenueOfRepair((BigDecimal) row[0]);
            if (groupByType.equals("week")) {
                revenueRepairDto.setDate((Date) row[1]);
            } else {
                revenueRepairDto.setStringDate(row[1].toString());
            }
            results.add(revenueRepairDto);
        }
        if (groupByType.equals("month")) Collections.reverse(results);
        return results;
    }

    @Override
    public PagingDataResponse adminGetAllRepairOrder(String code, String garageOwnerPhone, String garageName, Date fromDate, Date toDate,
                                                     Integer pageNumber, Integer pageSize) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select ro.id, ro.code, g.id as garage_id, g.name as garage_name, ro.created_by as garage_owner_phone, ro.appointment_date, q.total_price, ro.status from repair_orders ro ");
        jpql.append(" join garages g on ro.garage_id = g.id ");
        jpql.append(" left join quotations q on ro.id = q.repair_order_id ");
        jpql.append(" where 1 = 1 ");
        if (Objects.nonNull(code)) {
            jpql.append(" and ro.code like :code ");
            params.put("code", "%" + code + "%");
        }
        if (Objects.nonNull(garageOwnerPhone)) {
            jpql.append(" and ro.created_by like :garageOwnerPhone ");
            params.put("garageOwnerPhone", "%" + garageOwnerPhone + "%");
        }
        if (Objects.nonNull(garageName)) {
            jpql.append(" and g.name like :garageName ");
            params.put("garageName", "%" + garageName + "%");
        }
        this.setParamsDate(params, fromDate, toDate);
        jpql.append(" and ro.appointment_date between :fromDate and :toDate ");
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by ro.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "AdminRepairOrderDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    StringBuilder queryCommon(String groupByType, Long numberElement, Long garageId) {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append("WITH RECURSIVE date_range AS (\n" +
                "SELECT\n" +
                "\t0 AS n\n" +
                "UNION ALL\n" +
                "SELECT\n" +
                "\tn + 1\n" +
                "FROM\n" +
                "\tdate_range\n" +
                "WHERE\n" +
                "\tn < " + (numberElement - 1) + "\n" +
                "),");
        strQuery.append("need_revenue AS (\n" +
                "SELECT \n");

        String unitTime = "";
        if (groupByType.equals("month")) unitTime = "week";
        if (groupByType.equals("year")) unitTime = "month";

        if (unitTime.equals("week")) {
            strQuery.append(" WEEK(DATE(DATE_SUB(NOW(), INTERVAL 7 * n DAY))) + 1 AS week_number,\n" +
                    "\tYEAR(DATE(DATE_SUB(NOW(), INTERVAL 7 * n DAY))) AS year,\n" +
                    "\t0 AS revenue");
        }
        if (unitTime.equals("month")) {
            strQuery.append(" MONTH(DATE_ADD(NOW(), INTERVAL -1 * n MONTH)) AS month_number,\n" +
                    "\tYEAR(DATE_ADD(NOW(), INTERVAL -1 * n MONTH)) AS year,\n" +
                    "\t0 AS revenue");
        }
        strQuery.append(" FROM date_range \n");
        strQuery.append("UNION\n" +
                "SELECT \n");
        if (unitTime.equals("week")) {
            strQuery.append(" aw.week + 1 as week_number, \n");
        }
        if (unitTime.equals("month")) {
            strQuery.append(" aw.month AS month_number, \n");
        }
        strQuery.append("aw.year,\n" +
                "\tCOALESCE(SUM(q.total_price), 0) AS revenue \n");
        strQuery.append("FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tDISTINCT " + unitTime + "(created_at) AS "+ unitTime +",\n" +
                "\t\tYEAR(created_at) AS year\n" +
                "\tFROM\n" +
                "\t\trepair_orders) aw\n" +
                "LEFT JOIN\n" +
                "    repair_orders ro ON\n" +
                "\taw." + unitTime + " = " + unitTime + "(ro.created_at)\n" +
                "\t\tAND aw.year = YEAR(ro.created_at)\n" +
                "LEFT JOIN quotations q on ro.id = q.repair_order_id \n" +
                "\tWHERE\n" +
                "\t\tro.garage_id = " + garageId + "\n" +
                "\tGROUP BY\n" +
                "\t\t" + unitTime + "_number,\n" +
                "\t\tyear\n" +
                ")\n" +
                "SELECT\n" +
                "\tSUM(revenue) AS total_revenue,\n" +
                "\tCONCAT(" + unitTime + "_number, ' - ', year) AS string_date\n" +
                "FROM\n" +
                "\tneed_revenue\n" +
                "GROUP BY\n" +
                "\t" + unitTime + "_number,\n" +
                "\tyear\n" +
                "ORDER BY\n");
        if (unitTime.equals("week")) {
            strQuery.append("\tyear DESC,\n" +
                    "\t" + unitTime + "_number DESC\n");
        }
        if (unitTime.equals("month")) {
            strQuery.append("\tyear ASC,\n" +
                    "\t" + unitTime + "_number ASC\n");
        }
        strQuery.append("LIMIT " + numberElement + ";");

        return strQuery;
    }

    public void setParamsDate(Map<String, Object> params, Date fromDate, Date toDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (Objects.isNull(fromDate)) {
            params.put(FROM_DATE, "0000-00-00");
        } else {
            params.put(FROM_DATE, format.format(fromDate));
        }
        if (Objects.isNull(toDate)) {
            params.put(TO_DATE, "9999-12-12");
        } else {
            params.put(TO_DATE, format.format(toDate));
        }
    }

    public void setParamsDate(Map<String, Object> params, String fromDate, String toDate) {
        if (Objects.isNull(fromDate)) {
            params.put(FROM_DATE, "0000-00-00");
        } else {
            params.put(FROM_DATE, fromDate);
        }
        if (Objects.isNull(toDate)) {
            params.put(TO_DATE, "9999-12-12");
        } else {
            params.put(TO_DATE, toDate);
        }
    }
}
