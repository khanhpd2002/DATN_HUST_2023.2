package vn.com.cardoctor.garage_service.models.responses.service_history;

import vn.com.cardoctor.garage_service.models.responses.e_service.EServiceResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ServiceHistoryResponse {
    private Long repairOrderId;
    private EServiceResponse eServiceResponse;
    private BigDecimal price;
    private Date warrantyUntil;
}
