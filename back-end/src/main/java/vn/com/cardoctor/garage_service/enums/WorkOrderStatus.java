package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum WorkOrderStatus {
    IN_PROGRESS(0),

    PENDING(1),
    FINISHED(2),
    CANCELED(3);
    private final Integer code;
    private static Map<Integer, WorkOrderStatus> workOrderStatusMap;


    WorkOrderStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static WorkOrderStatus getWorkOrderStatus(Integer code) {
        if (workOrderStatusMap == null) {
            workOrderStatusMap = new HashMap<>();
            workOrderStatusMap.put(0, WorkOrderStatus.IN_PROGRESS);
            workOrderStatusMap.put(1, WorkOrderStatus.PENDING);
            workOrderStatusMap.put(2, WorkOrderStatus.FINISHED);
            workOrderStatusMap.put(3, WorkOrderStatus.CANCELED);
        }
        return workOrderStatusMap.get(code);
    }
}
