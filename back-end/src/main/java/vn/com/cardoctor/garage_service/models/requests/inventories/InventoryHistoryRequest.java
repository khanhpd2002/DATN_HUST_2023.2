package vn.com.cardoctor.garage_service.models.requests.inventories;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InventoryHistoryRequest {
    private List<Counter> countersName;
    private List<Approver> approvers;
    private Date countDate;
    private Date adjustmentDate;
    private List<InventoryHistoryDetailRequest> inventoryHistoryDetails;

    @lombok.Data
    public static class Counter {
        private long id;
        private String counterName;
    }

    @lombok.Data
    public static class Approver {
        private long id;
        private String approverName;
    }
}
