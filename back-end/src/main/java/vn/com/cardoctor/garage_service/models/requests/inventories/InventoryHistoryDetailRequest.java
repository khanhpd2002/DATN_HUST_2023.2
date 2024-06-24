package vn.com.cardoctor.garage_service.models.requests.inventories;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryHistoryDetailRequest {
    private Long productId;
    private BigDecimal realityUnitPrice;
    private BigDecimal systemUnitPrice;
    private BigDecimal systemInventory;
    private BigDecimal realityInventory;
}
