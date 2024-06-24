package vn.com.cardoctor.garage_service.repositories.impls;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDetailDto;
import vn.com.cardoctor.garage_service.repositories.InventoryHistoryDetailRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryHistoryDetailRepositoryImpl implements InventoryHistoryDetailRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<InventoryHistoryDetailDto> findHistoryDetail(Long inventoryHistoryId, boolean isDifferentQuantity) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        jpql.append("select ihd.*, p.code as product_code, p.name as product_name, p.unit as unit, (ihd.system_unit_price * ihd.system_inventory) as price_system_inventory, ");
        jpql.append("(ihd.reality_unit_price * ihd.reality_inventory) as price_reality_inventory, (ihd.system_inventory - ihd.reality_inventory) as difference_quantity, ");
        jpql.append("(ihd.system_unit_price * ihd.system_inventory - ihd.reality_unit_price * ihd.reality_inventory) as difference_price ");
        jpql.append("   from inventory_histories_detail ihd ");
        jpql.append("   join products p on ihd.product_id = p.id ");
        jpql.append("   where ihd.inventory_history_id = :inventoryHistoryId ");
        if (isDifferentQuantity) {
            jpql.append(" and ihd.system_inventory <> ihd.reality_inventory ");
        }

        params.put("inventoryHistoryId", inventoryHistoryId);

        Query q = entityManager.createNativeQuery(jpql.toString(), "InventoryHistoryDetailDto");
        params.forEach(q::setParameter);

        return q.getResultList();
    }
}
