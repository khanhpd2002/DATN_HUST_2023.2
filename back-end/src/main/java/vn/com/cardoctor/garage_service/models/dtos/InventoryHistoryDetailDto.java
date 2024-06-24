package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryDetailDto {
    private long id;
    private Long productId;
    private String productCode;
    private String productName;
    private String unit;
    private BigDecimal systemUnitPrice;
    private BigDecimal realityUnitPrice;
    private BigDecimal systemInventory;
    private BigDecimal priceSystemInventory;
    private BigDecimal realityInventory;
    private BigDecimal priceRealityInventory;
    private BigDecimal differenceQuantity;
    private BigDecimal differencePrice;
    private Long inventoryHistoryId;
}
