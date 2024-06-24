package vn.com.cardoctor.garage_service.models.requests.service_history;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ServiceHistoryRequest {
    private Long repairOrderId;
    private Long serviceId;
    private BigDecimal price;
    private Date warrantyUntil;
}
