package vn.com.cardoctor.garage_service.models.responses.invoice;

import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
import lombok.Data;

@Data
public class InvoiceResponse {
    RepairOrderResponse repairOrderResponse;
    int paymentStatus;
}
