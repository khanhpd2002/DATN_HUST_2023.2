package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.enums.ProductTypeEnum;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;
import vn.com.cardoctor.garage_service.models.responses.product.ConfigPriceResponse;
import vn.com.cardoctor.garage_service.repositories.ConfigPriceRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class ConfigPriceRepositoryImpl implements ConfigPriceRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public PagingDataResponse findAllConfigPrice(Long garageId, Long type, String code,
                                                  String name, Integer pageSize, Integer pageNumber) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append(" select cp.*, ct.customer_type_name, p.name as product_name, p.code as product_code, gs.name as garage_service_name, gs.code as garage_service_code, ");
        jpql.append(" p.unit from config_prices cp ");
        jpql.append(" join (select MIN(id) as min_id from config_prices ");
        jpql.append(" group by product_id, garage_service_id) subquery on cp.id = subquery.min_id ");
        jpql.append("   join customer_types ct on cp.customer_type_id = ct.id ");
        jpql.append(" left join products p on cp.product_id = p.id ");
        jpql.append(" left join garage_services gs on cp.garage_service_id = gs.id ");
        jpql.append(" where 1 = 1 ");

        if (Objects.nonNull(garageId)) {
            jpql.append("   and cp.garage_id = :garageId ");
            params.put("garageId", garageId);
        }
        if (Objects.nonNull(type)) {
            jpql.append("   and cp.type = :type ");
            params.put("type", type);
        }
        if (Objects.nonNull(code)) {
            jpql.append("   and (p.code like :code or gs.code like :code) ");
            params.put("code", "%" + code + "%");
        }
        if (Objects.nonNull(name)) {
            jpql.append("   and (p.name like :name or gs.name like :name) ");
            params.put("name", "%" + name + "%");
        }

        Query countQuery = entityManager.createNativeQuery("select count(*) from (" + jpql + ") a");
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        jpql.append(" order by cp.created_at desc limit :limit offset :offset ");
        pageNumber = pageNumber != null ? pageNumber - 1 : 0;
        int limit = pageSize != null ? pageSize : 20;
        int offset = (pageNumber >= 0 ? pageNumber : 0) * limit;
        params.put("limit", limit);
        params.put("offset", offset);

        Query q = entityManager.createNativeQuery(jpql.toString(), "ConfigPriceDto");
        params.forEach(q::setParameter);

        List<ConfigPriceDto> configPriceDtos = q.getResultList();
        List<ConfigPriceResponse> responses = new ArrayList<>();
        for (ConfigPriceDto configPriceDto : configPriceDtos) {
            ConfigPriceResponse response = new ConfigPriceResponse();
            response.setType(configPriceDto.getType());
            response.setProductId(configPriceDto.getProductId());
            response.setProductName(configPriceDto.getProductName());
            response.setProductCode(configPriceDto.getProductCode());
            response.setGarageServiceId(configPriceDto.getGarageServiceId());
            response.setGarageServiceName(configPriceDto.getGarageServiceName());
            response.setGarageServiceCode(configPriceDto.getGarageServiceCode());
            List<ConfigPriceDto> listConfigPrice = this.findConfigPriceByProductAndService(configPriceDto.getProductId(), configPriceDto.getGarageServiceId());
            response.setConfigPrices(listConfigPrice);
            response.setGarageId(garageId);
            responses.add(response);
        }

        PagingDataResponse response = new PagingDataResponse();
        response.setCurrentPage(pageNumber + 1);
        response.setTotalElement(total);
        response.setPageSize(limit);
        response.setData(responses);
        return response;
    }

    @Override
    public List<ConfigPriceDto> findConfigPriceByProductAndService(Long productId, Long garageServiceId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select cp.*, ct.customer_type_name, p.code as product_code, p.name as product_name, p.unit, ");
        jpql.append(" gs.name as garage_service_name, gs.code as garage_service_code from config_prices cp ");
        jpql.append("   join customer_types ct on cp.customer_type_id = ct.id ");
        jpql.append(" left join products p on cp.product_id = p.id ");
        jpql.append(" left join garage_services gs on cp.garage_service_id = gs.id ");

        if (Objects.nonNull(productId)) {
            jpql.append("   where cp.product_id = :productId");
            params.put("productId", productId);
        }
        if (Objects.nonNull(garageServiceId)) {
            jpql.append("   where cp.garage_service_id = :garageServiceId");
            params.put("garageServiceId", garageServiceId);
        }


        Query q = entityManager.createNativeQuery(jpql.toString(), "ConfigPriceDto");
        params.forEach(q::setParameter);
        List<ConfigPriceDto> response = q.getResultList();
        return response;
    }

    @Override
    public ConfigPriceDto findByCustomerTypeAndProductAndService(Long customerTypeId, Long modelId, Integer type) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append(" select cp.*, null as customer_type_name, p.name as product_name, p.code as product_code, gs.name as garage_service_name, gs.code as garage_service_code ");
        jpql.append(" ,p.unit from config_prices cp ");
        jpql.append(" left join products p on cp.product_id = p.id ");
        jpql.append(" left join garage_services gs on cp.garage_service_id = gs.id ");
        jpql.append(" where 1 = 1 ");
        jpql.append(" and cp.type = :type ");
        jpql.append(" and cp.customer_type_id = :customerTypeId ");
        if (type.equals(ProductTypeEnum.SPARE_PART.getCode())) {
            jpql.append(" and p.id = :modelId ");
        }
        if (type.equals(ProductTypeEnum.SERVICE.getCode())) {
            jpql.append(" and gs.id = :modelId ");
        }
        params.put("modelId", modelId);
        params.put("customerTypeId", customerTypeId);
        params.put("type", type);

        Query q = entityManager.createNativeQuery(jpql.toString(), "ConfigPriceDto");
        params.forEach(q::setParameter);
        ConfigPriceDto response;
        try {
            response = (ConfigPriceDto) q.getSingleResult();
        } catch (Exception e) {
            response = new ConfigPriceDto();
        }
        return response;
    }
}
