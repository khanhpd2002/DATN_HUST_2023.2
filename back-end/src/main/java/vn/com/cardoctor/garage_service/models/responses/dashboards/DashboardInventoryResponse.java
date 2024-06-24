package vn.com.cardoctor.garage_service.models.responses.dashboards;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardInventoryResponse {
    private Long totalProductCode;
    private Long totalProductQuantity;
    private BigDecimal inventoryValue;
    private Long totalTicketHasNotDelivery;
}
