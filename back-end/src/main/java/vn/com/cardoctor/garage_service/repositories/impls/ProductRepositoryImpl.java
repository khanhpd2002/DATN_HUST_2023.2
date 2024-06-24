package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.entities.dashboards.LongTermInventoryProduct;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductHistoryDto;
import vn.com.cardoctor.garage_service.models.responses.dashboards.LowInventoryProduct;
import vn.com.cardoctor.garage_service.repositories.ProductRepositoryCustom;
import vn.com.cardoctor.garage_service.utils.DataUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findAllProduct(Integer sparePartType, String code, String name, Long carBrandId, Long carModelId, Long carYearId, Long carVersionId,
                                             Long distributorId, Long inventoryId, Long garageId, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select jp.id, p.code, p.name, jp.parent_product_id, jp.distributor_id, jp.purchase_price, p.quantity, p.unit, ");
        jpql.append(" p.spare_part_type, d.code as distributor_code, d.name as distributor_name from products p ");
        jpql.append(" join products jp on p.id = jp.parent_product_id ");
        jpql.append(" left join distributors d on jp.distributor_id = d.id ");
        if (Objects.nonNull(carBrandId) || Objects.nonNull(carModelId) || Objects.nonNull(carYearId) || Objects.nonNull(carVersionId)) {
            jpql.append(" left join product_compatibilities pc on p.id = pc.product_id ");
        }
        if (Objects.nonNull(carBrandId)) {
            jpql.append(" left join `cardoctor_cms`.car_brands cccb on pc.car_brand_id = cccb.id ");
        }
        if (Objects.nonNull(carModelId)) {
            jpql.append(" left join `cardoctor_cms`.car_models cccm on pc.car_model_id = cccm.id ");
        }
        if (Objects.nonNull(carYearId)) {
            jpql.append(" left join `cardoctor_cms`.car_years cccy on pc.car_year_id = cccy.id ");
        }
        if (Objects.nonNull(carVersionId)) {
            jpql.append(" left join `cardoctor_cms`.car_versions cccv on pc.car_version_id = cccv.id ");
        }
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(inventoryId)) {
            params.put("inventoryId", inventoryId);
            jpql.append("   and p.inventory_id = :inventoryId ");
        }
        if (Objects.nonNull(sparePartType)) {
            params.put("sparePartType", sparePartType);
            jpql.append("   and p.spare_part_type = :sparePartType ");
        }
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and p.garage_id = :garageId ");
        }
        if (Objects.nonNull(code)) {
            params.put("code", "%" + code + "%");
            jpql.append("   and lower(p.code) like :code ");
        }
        if (Objects.nonNull(name)) {
            params.put("name", "%" + name + "%");
            jpql.append("   and lower(p.name) like :name ");
        }
        if (Objects.nonNull(carBrandId)) {
            params.put("carBrandId", carBrandId);
            jpql.append("   and pc.car_brand_id = :carBrandId ");
        }
        if (Objects.nonNull(carModelId)) {
            params.put("carModelId", carModelId);
            jpql.append("   and pc.car_model_id = :carModelId ");
        }
        if (Objects.nonNull(carYearId)) {
            params.put("carYearId", carYearId);
            jpql.append("   and pc.car_year_id = :carYearId ");
        }
        if (Objects.nonNull(carVersionId)) {
            params.put("carVersionId", carVersionId);
            jpql.append("   and pc.car_version_id = :carVersionId ");
        }
        if (Objects.nonNull(distributorId)) {
            params.put("distributorId", distributorId);
            jpql.append("   and jp.distributor_id = :distributorId ");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" group by jp.id order by p.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "ProductDto");
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public PagingDataResponse findAllParentProduct(Long inventoryId, Long distributorId, Integer sparePartType, String code, String name, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select p.id, p.code, p.name, p.parent_product_id, p.distributor_id,  ");
        if (Objects.nonNull(distributorId)) {
            jpql.append(" p1.purchase_price, ");
        } else {
            jpql.append(" p.purchase_price, ");
        }
        jpql.append(" p.quantity, p.unit, p.spare_part_type, p.inventory_id, p.garage_id, p.first_sell_at, p.created_at, p.updated_at ");
        jpql.append(" from products p ");
        if (Objects.nonNull(distributorId)) {
            jpql.append(" join products p1 on p1.parent_product_id = p.id ");
        }
        jpql.append(" where p.parent_product_id is null ");
        jpql.append(" and p.inventory_id = :inventoryId ");
        if (Objects.nonNull(distributorId) && !distributorId.equals(0L)) {
            jpql.append(" and p1.distributor_id = :distributorId ");
            params.put("distributorId", distributorId);
        }
        params.put("inventoryId", inventoryId);
        if (Objects.nonNull(sparePartType)) {
            jpql.append("   and p.spare_part_type = :sparePartType ");
            params.put("sparePartType", sparePartType);
        }
        if (Objects.nonNull(code)) {
            jpql.append("   and p.code like :code ");
            params.put("code", "%" + code + "%");
        }
        if (Objects.nonNull(name)) {
            jpql.append("   and p.name like :name ");
            params.put("name", "%" + name + "%");
        }
        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by p.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), Product.class);
        params.forEach(q::setParameter);

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(q.getResultList());
        return response;
    }

    @Override
    public List<ProductHistoryDto> getAveragePriceProduct(Long inventoryId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("SELECT\n" +
                "    p.id,\n" +
                "    p.code,\n" +
                "    p.name,\n" +
                "    p.unit,\n" +
                "    SUM(product_price.total_price) / SUM(product_price.total_quantity) AS reality_unit_price,\n" +
                "    p.purchase_price AS system_unit_price,\n" +
                "    p.quantity\n" +
                "FROM\n" +
                "    products p\n" +
                "LEFT JOIN\n" +
                "    (\n" +
                "        SELECT\n" +
                "            obj.product_id,\n" +
                "            SUM(price) AS total_price,\n" +
                "            SUM(obj.quantity) AS total_quantity\n" +
                "        FROM\n" +
                "            orders_distributor\n" +
                "        CROSS JOIN\n" +
                "            JSON_TABLE(products, '$[*]'\n" +
                "                COLUMNS (\n" +
                "                    product_id LONG PATH '$.productId',\n" +
                "                    price FLOAT PATH '$.price',\n" +
                "                    quantity INT PATH '$.quantity'\n" +
                "                )\n" +
                "            ) obj\n" +
                "        GROUP BY\n" +
                "            obj.product_id\n" +
                "    ) product_price\n" +
                "ON\n" +
                "    p.id = product_price.product_id\n" +
                "WHERE\n" +
                "    p.parent_product_id IS NULL\n" +
                "    AND p.inventory_id = :inventoryId\n" +
                "GROUP BY\n" +
                "    p.id, p.code, p.name, p.unit, p.purchase_price, p.quantity");
        params.put("inventoryId", inventoryId);
        Query q = this.entityManager.createNativeQuery(jpql.toString(), "ProductHistoryDto");
        params.forEach(q::setParameter);
        return q.getResultList();
    }

    @Override
    public ProductDto findProductById(Long productId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select jp.id, p.code, p.name, jp.parent_product_id, jp.distributor_id, jp.purchase_price, p.quantity, p.unit, ");
        jpql.append(" p.spare_part_type, d.code as distributor_code, d.name as distributor_name from products p ");
        jpql.append(" join products jp on p.id = jp.parent_product_id ");
        jpql.append(" left join distributors d on jp.distributor_id = d.id ");
        jpql.append("   where jp.id = :productId ");
        params.put("productId", productId);

        Query q = entityManager.createNativeQuery(jpql.toString(), "ProductDto");
        params.forEach(q::setParameter);
        ProductDto response;
        try {
            response = (ProductDto) q.getSingleResult();
        } catch (Exception e) {
            response = new ProductDto();
        }
        return response;
    }

    @Override
    public List<LowInventoryProduct> findAllLowInventoryProducts(Long garageId, Long quantityLabel) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" SELECT p.id, p.quantity, ");
        jpql.append(" SUM(CASE WHEN ip.status = 1 AND it.`type` = 1 THEN ip.export_quantity ELSE 0 END) AS total_inbound, ");
        jpql.append(" GREATEST(COALESCE(TIMESTAMPDIFF(MONTH, p.first_sell_at, CURDATE()), 0) + 1, 1) AS months_since_first_sell, ");
        jpql.append(" GREATEST(SUM(CASE WHEN ip.status = 1 AND it.`type` = 1 THEN ip.export_quantity ELSE 0 END) - p.quantity, 0) AS quantity_sold, ");
        jpql.append(" GREATEST(SUM(CASE WHEN ip.status = 1 AND it.`type` = 1 THEN ip.export_quantity ELSE 0 END) - p.quantity, 0) " +
                "/ GREATEST(COALESCE(TIMESTAMPDIFF(MONTH, p.first_sell_at, CURDATE()), 0) + 1, 1) AS consumption_per_month, ");
        jpql.append(" CASE \n" +
                "        WHEN GREATEST(SUM(CASE WHEN ip.status = 1 THEN ip.export_quantity ELSE 0 END) - p.quantity, 0) > 0 \n" +
                "        THEN p.quantity / GREATEST(SUM(CASE WHEN ip.status = 1 THEN ip.export_quantity ELSE 0 END) - p.quantity, 0) " +
                " * GREATEST(COALESCE(TIMESTAMPDIFF(MONTH, p.first_sell_at, CURDATE()), 0) + 1, 1)\n" +
                "        ELSE 0\n" +
                "    END AS months_to_consume_inventory, \n");
        jpql.append(" p.name ");
        jpql.append(" FROM products p ");
        jpql.append(" LEFT JOIN inbound_products ip ON ip.product_id = p.id ");
        jpql.append(" LEFT JOIN inbound_tickets it ON it.id = ip.inbound_ticket_id ");
        jpql.append("   where 1 = 1 ");
        if (Objects.nonNull(garageId)) {
            params.put("garageId", garageId);
            jpql.append("   and p.garage_id = (:garageId) ");
        }
        jpql.append("   and p.parent_product_id IS NULL ");
        jpql.append("   and p.first_sell_at is not null ");
        jpql.append(" GROUP BY p.id HAVING quantity_sold > 0 ORDER BY months_to_consume_inventory ASC ");
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
            List<LowInventoryProduct> result = new ArrayList<>();
            for (Object[] row : rows) {
                LowInventoryProduct lowInventoryProduct = new LowInventoryProduct();
                lowInventoryProduct.setProductId(DataUtils.safeToLong(row[0]));
                lowInventoryProduct.setQuantity(DataUtils.safeToBigDecimal(row[1]));
                lowInventoryProduct.setTotalInbound(DataUtils.safeToBigDecimal(row[2]));
                lowInventoryProduct.setMonthsSinceFirstSell(DataUtils.safeToBigDecimal(row[3]));
                lowInventoryProduct.setQuantitySold(DataUtils.safeToBigDecimal(row[4]));
                lowInventoryProduct.setConsumptionPerMonth(DataUtils.safeToBigDecimal(row[5]));
                lowInventoryProduct.setMonthsToConsumeInventory(DataUtils.safeToBigDecimal(row[6]));
                lowInventoryProduct.setProductName(DataUtils.safeToString(row[7]));

                result.add(lowInventoryProduct);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<LongTermInventoryProduct> findAllLongTermInventoryProducts(Long garageId, Pageable pageable) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("WITH InventoryHistory AS ( ");
        jpql.append(" SELECT\n" +
                "\tp.id AS product_id,\n" +
                "\tp.name,\n" +
                "\tp.code,\n" +
                "\tip.updated_at AS sale_date,\n" +
                "\tip.export_quantity AS sale_quantity,\n" +
                "\tCOALESCE(SUM(CASE WHEN ip.status = 1 AND it.`type` = 1 THEN ip.export_quantity ELSE 0 END) " +
                "OVER (PARTITION BY p.id ORDER BY ip.updated_at ASC ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW), 0) AS cumulative_import_quantity,\n" +
                "\tROW_NUMBER() OVER (PARTITION BY p.id ");
        jpql.append(" ORDER BY ip.updated_at) AS rn1 \n");
        jpql.append(" FROM products p \n");
        jpql.append(" JOIN inbound_products ip ON ip.product_id = p.id \n");
        jpql.append(" LEFT JOIN inbound_tickets it ON it.id = ip.inbound_ticket_id \n");
        jpql.append("   where 1 = 1 \n");
        jpql.append(" and p.parent_product_id IS NULL \n");
        params.put("garageId", garageId);
        jpql.append(" and p.garage_id = :garageId");
        jpql.append(" GROUP BY\n" +
                "\tp.id,\n" +
                "\tip.updated_at,\n" +
                "\tip.export_quantity\n" +
                "), \n");
        jpql.append(" RankedData AS ( \n");
        jpql.append(" SELECT\n" +
                "\tih.*,\n" +
                "\tROW_NUMBER() OVER (PARTITION BY ih.product_id \n");
        jpql.append(" ORDER BY\n" +
                "\tih.cumulative_import_quantity) AS rn,\n" +
                "\tGREATEST(SUM(CASE WHEN ip.status = 1 AND it.`type` = 1 THEN ip.export_quantity ELSE 0 END) - p.quantity,\n" +
                "\t0) AS quantity_sold \n");
        jpql.append(" FROM InventoryHistory ih \n");
        jpql.append(" LEFT JOIN inbound_products ip ON ip.product_id = ih.product_id \n");
        jpql.append(" LEFT JOIN products p ON ih.product_id = p.id \n");
        jpql.append(" LEFT JOIN inbound_tickets it ON it.id = ip.inbound_ticket_id \n");
        jpql.append(" WHERE p.parent_product_id IS NULL \n");
        jpql.append(" GROUP BY ih.product_id, ih.sale_date \n");
        jpql.append(" HAVING ih.cumulative_import_quantity > quantity_sold \n");
        jpql.append(" ORDER BY ih.product_id ASC, ih.cumulative_import_quantity ASC ) \n");
        jpql.append(" SELECT product_id,\n" +
                "\tname,\n" +
                "\tcode,\n" +
                "\tsale_date,\n" +
                "\tcumulative_import_quantity,\n" +
                "\tsale_quantity,\n" +
                "\tquantity_sold \n");
        jpql.append(" FROM RankedData \n");
        jpql.append("   where 1 = 1 ");
        jpql.append(" and rn = 1 \n");
        jpql.append(" ORDER BY sale_date ASC");

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
            if (!pageable.isUnpaged()) {
                int size = pageable.getPageSize();
                int page = pageable.getPageNumber();
                selectQuery.setFirstResult(page * size);
                selectQuery.setMaxResults(size);
            }
            List<Object[]> rows = selectQuery.getResultList();
            List<LongTermInventoryProduct> result = new ArrayList<>();
            for (Object[] row : rows) {
                LongTermInventoryProduct longTermInventoryProduct = new LongTermInventoryProduct();

                longTermInventoryProduct.setProductId(DataUtils.safeToLong(row[0]));
                longTermInventoryProduct.setProductName(DataUtils.safeToString(row[1]));
                longTermInventoryProduct.setProductCode(DataUtils.safeToString(row[2]));
                if(row[3] != null) {
                    longTermInventoryProduct.setInboundDate(((Timestamp) row[3]).toLocalDateTime());
                }
                result.add(longTermInventoryProduct);
            }
            return new PageImpl<>(result, pageable, total);
        }

        return new PageImpl<>(new ArrayList<>(), pageable, total);


    }


}
