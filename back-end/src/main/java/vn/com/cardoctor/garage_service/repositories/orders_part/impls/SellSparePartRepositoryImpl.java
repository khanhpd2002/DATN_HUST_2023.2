package vn.com.cardoctor.garage_service.repositories.orders_part.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartFilter;
import vn.com.cardoctor.garage_service.models.responses.dashboards.RevenueSellSparePart;
import vn.com.cardoctor.garage_service.repositories.orders_part.SellSparePartRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.QueryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class SellSparePartRepositoryImpl implements SellSparePartRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PagingDataResponse findAllSellSparePart(Long garageId, SellSparePartFilter sparePartFilter, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select ssp.*, c.full_name as customer_name from sell_spare_parts ssp ");
        jpql.append("   join customers c on ssp.customer_id = c.id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            jpql.append("   and ssp.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(sparePartFilter.getCustomerTypeId())) {
            jpql.append("   and c.customer_type_id = :customerTypeId ");
            params.put("customerTypeId", sparePartFilter.getCustomerTypeId());
        }
        if (Objects.nonNull(sparePartFilter.getCustomerPhone())) {
            jpql.append("   and c.phone_number like :customerPhone ");
            params.put("customerPhone", "%" + sparePartFilter.getCustomerPhone() + "%");
        }
        if (Objects.nonNull(sparePartFilter.getCustomerName())) {
            jpql.append("   and lower(c.full_name) like lower(:customerName) ");
            params.put("customerName", "%" + sparePartFilter.getCustomerName() + "%");
        }
        if (Objects.nonNull(sparePartFilter.getFilter())) {
            jpql.append("   and lower(concat_ws('-', ssp.sell_code, c.full_name)) like lower(:filter) ");
            params.put("filter", "%" + sparePartFilter.getFilter() + "%");
        }
        QueryUtil.buildQueryParams(sparePartFilter.getFromDate(), sparePartFilter.getToDate(), params);
        jpql.append("   and  date_format(ssp.created_at, '%Y-%m-%d') between :fromDate and :toDate ");
        if (Objects.nonNull(sparePartFilter.getDeliveryStatus())) {
            jpql.append("   and ssp.delivery_status = :deliveryStatus ");
            params.put("deliveryStatus", sparePartFilter.getDeliveryStatus());
        }
        if (Objects.nonNull(sparePartFilter.getPaymentStatus())) {
            jpql.append("   and ssp.payment_status = :paymentStatus ");
            params.put("paymentStatus", sparePartFilter.getPaymentStatus());
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by ssp.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "SellSparePartDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public BigDecimal getTotalPriceByGarageId(Long garageId, String fromDate, String toDate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        sb.append("select COALESCE(sum(total_price),0) from sell_spare_parts ");
        sb.append("where 1 = 1 ");

        if (Objects.nonNull(garageId)) {
            sb.append("and garage_id = :garageId ");
            params.put("garageId", garageId);
        }

        sb.append("and DATE(created_at) between :fromDate and :toDate ");
        params.put("fromDate", Objects.isNull(fromDate) ? "0000-00-00" : fromDate);
        params.put("toDate", Objects.isNull(toDate) ? "9999-12-12" : toDate);

        Query selectQuery = entityManager.createNativeQuery(sb.toString());
        params.forEach(selectQuery::setParameter);

        return (BigDecimal) selectQuery.getSingleResult();
    }

    @Override
    public List<RevenueSellSparePart> findRevenueOfSellSparePart(String groupByType, String fromDate, String toDate, Long garageId) {
        StringBuilder strQuery = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        String select = "";
        String groupBy = "";
        String orderBy = "";
        if (groupByType.equals("week")) {
            select = " select sum(ssp.total_price) as revenue_of_ssp, date(ssp.created_at) as date, max(month(ssp.created_at)) as string_date from sell_spare_parts ssp \n";
            groupBy = " GROUP BY date";
        } else if (groupByType.equals("month")) {
            select = " select sum(ssp.total_price) as revenue_of_ssp, max(date(ssp.created_at)) as date, DATE_FORMAT(ssp.created_at,'%v-%x') AS string_date from sell_spare_parts ssp \n";
            groupBy = " GROUP BY string_date";
            orderBy = " ORDER BY date";
        } else if (groupByType.equals("year")) {
            select = " select sum(ssp.total_price) as revenue_of_ssp, max(date(ssp.created_at)) as date, DATE_FORMAT(ssp.created_at,'%m-%Y') AS string_date from sell_spare_parts ssp\n";
            groupBy = " GROUP BY string_date";
            orderBy = " ORDER BY date";
        }
        strQuery.append(select);
        strQuery.append(" WHERE :fromDate <= date(ssp.created_at) and date(ssp.created_at) <= date_add(:toDate, interval 1 day ) ");
        strQuery.append(" and ssp.garage_id = :garageId ");
        strQuery.append(groupBy);
        strQuery.append(orderBy);

        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("garageId", garageId);

        Query q = entityManager.createNativeQuery(strQuery.toString());
        params.forEach(q::setParameter);
        List<Object[]> rawResults = q.getResultList();
        List<RevenueSellSparePart> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            RevenueSellSparePart revenueSellSparePart = new RevenueSellSparePart();
            revenueSellSparePart.setRevenueSellSparePart((BigDecimal) row[0]);
            revenueSellSparePart.setDate((Date) row[1]);
            revenueSellSparePart.setStringDate(row[2].toString());
            results.add(revenueSellSparePart);
        }
        return results;
    }

    @Override
    public List<RevenueSellSparePart> findRevenueOfSellSparePartV2(String groupByType, Long garageId) {
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
                    "COALESCE(SUM(ssp.total_price), 0) AS revenue_of_ssp,\n" +
                    "    ds.date_value AS date\n" +
                    "FROM\n" +
                    "    date_series ds\n" +
                    "LEFT JOIN\n" +
                    "    sell_spare_parts ssp ON ds.date_value = DATE(ssp.created_at) AND ssp.garage_id = " + garageId + "\n" +
                    "GROUP BY\n" +
                    "    ds.date_value");
        }
        if (groupByType.equals("month")) {
            strQuery = queryCommon(groupByType, Long.valueOf(4), garageId);
        }
        if (groupByType.equals("year")) {
            strQuery = queryCommon(groupByType, Long.valueOf(12), garageId);
        }

        Query q = entityManager.createNativeQuery(strQuery.toString());
        List<Object[]> rawResults = q.getResultList();
        List<RevenueSellSparePart> results = new ArrayList<>();
        for (Object[] row : rawResults) {
            RevenueSellSparePart revenueSellSparePart = new RevenueSellSparePart();

            revenueSellSparePart.setRevenueSellSparePart((BigDecimal) row[0]);
            if (groupByType.equals("week")) {
                revenueSellSparePart.setDate((Date) row[1]);
            } else {
                revenueSellSparePart.setStringDate(row[1].toString());
            }
            results.add(revenueSellSparePart);
        }
        if (groupByType.equals("month")) Collections.reverse(results);
        return results;
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
                "\tCOALESCE(SUM(total_price), 0) AS revenue \n");
        strQuery.append("FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tDISTINCT " + unitTime + "(created_at) AS "+ unitTime +",\n" +
                "\t\tYEAR(created_at) AS year\n" +
                "\tFROM\n" +
                "\t\tsell_spare_parts) aw\n" +
                "LEFT JOIN\n" +
                "    sell_spare_parts ssp ON\n" +
                "\taw." + unitTime + " = " + unitTime + "(ssp.created_at)\n" +
                "\t\tAND aw.year = YEAR(ssp.created_at)\n" +
                "\tWHERE\n" +
                "\t\tssp.garage_id = " + garageId + "\n" +
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
}
