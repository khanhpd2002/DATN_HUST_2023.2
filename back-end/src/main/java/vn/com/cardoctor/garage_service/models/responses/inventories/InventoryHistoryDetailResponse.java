package vn.com.cardoctor.garage_service.models.responses.inventories;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDetailDto;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;

import java.util.Date;
import java.util.List;

@Data
public class InventoryHistoryDetailResponse {
    private Date countDate;
    private List<InventoryHistoryRequest.Counter> countersName;
    private List<InventoryHistoryRequest.Approver> approvers;
    private List<InventoryHistoryDetailDto> inventoryHistoryDetails;
    private Boolean canAdjustment;
}
