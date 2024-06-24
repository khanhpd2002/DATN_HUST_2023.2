package vn.com.cardoctor.garage_service.models.responses.work_order;

import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderResponse {
    RepairOrderResponse repairOrderResponse;
    Date scheduledDate;
    Date actualEndDate;
}
