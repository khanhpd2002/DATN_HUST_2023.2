package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDetailDto;

import javax.persistence.*;
import java.math.BigDecimal;

@SqlResultSetMapping(
        name = "InventoryHistoryDetailDto",
        classes = {
                @ConstructorResult(
                        targetClass = InventoryHistoryDetailDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "product_id", type = Long.class),
                                @ColumnResult(name = "product_code", type = String.class),
                                @ColumnResult(name = "product_name", type = String.class),
                                @ColumnResult(name = "unit", type = String.class),
                                @ColumnResult(name = "system_unit_price", type = BigDecimal.class),
                                @ColumnResult(name = "reality_unit_price", type = BigDecimal.class),
                                @ColumnResult(name = "system_inventory", type = BigDecimal.class),
                                @ColumnResult(name = "price_system_inventory", type = BigDecimal.class),
                                @ColumnResult(name = "reality_inventory", type = BigDecimal.class),
                                @ColumnResult(name = "price_reality_inventory", type = BigDecimal.class),
                                @ColumnResult(name = "difference_quantity", type = BigDecimal.class),
                                @ColumnResult(name = "difference_price", type = BigDecimal.class),
                                @ColumnResult(name = "inventory_history_id", type = Long.class),
                        })})


@Entity
@Data
@Table(name = "inventory_histories_detail")
public class InventoryHistoryDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Long productId;
    private BigDecimal systemUnitPrice;
    private BigDecimal realityUnitPrice;
    private BigDecimal systemInventory;
    private BigDecimal realityInventory;
    private Long inventoryHistoryId;
}
