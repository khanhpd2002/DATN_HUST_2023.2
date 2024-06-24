package vn.com.cardoctor.garage_service.models.responses.dashboards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowInventoryProduct {
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal totalInbound;
    private BigDecimal monthsSinceFirstSell;
    private BigDecimal quantitySold;
    private BigDecimal monthsToConsumeInventory;
    private BigDecimal consumptionPerMonth;
    private String productName;
}
