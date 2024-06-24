package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum RepairOrderStatus {
    NEW(0),
    RECEIVED(1), // Sau khi tạo phiếu -> Đã nhận xe
    IN_DIAGNOSIS(2),
    QUOTATION_SEND(3), // Sau khi bấm báo giá -> Báo giá
    WORK_IN_PROGRESS(4), // Sau khi bấm tiến hành sửa chữa -> Đang tiến hành
    COMPLETE_WORK_ORDER(5), // Sau khi bấm Hoàn tất dịch vụ -> Thanh toán
    DONE(6), // Sau khi bấm Bàn giao xe -> Bàn giao xe
    GARAGE_CANCELED(7),
    CUSTOMER_CANCELED(8); // Sau khi bấm Huỷ -> Huỷ
    private final Integer code;
    private static Map<Integer, RepairOrderStatus> repairOrderStatusMap;


    RepairOrderStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static RepairOrderStatus getRepairOrderStatus(Integer code) {
        if (repairOrderStatusMap == null) {
            repairOrderStatusMap = new HashMap<>();
            repairOrderStatusMap.put(0, RepairOrderStatus.NEW);
            repairOrderStatusMap.put(1, RepairOrderStatus.RECEIVED);
            repairOrderStatusMap.put(2, RepairOrderStatus.IN_DIAGNOSIS);
            repairOrderStatusMap.put(3, RepairOrderStatus.QUOTATION_SEND);
            repairOrderStatusMap.put(4, RepairOrderStatus.WORK_IN_PROGRESS);
            repairOrderStatusMap.put(5, RepairOrderStatus.COMPLETE_WORK_ORDER);
            repairOrderStatusMap.put(6, RepairOrderStatus.DONE);
            repairOrderStatusMap.put(7, RepairOrderStatus.GARAGE_CANCELED);
            repairOrderStatusMap.put(8, RepairOrderStatus.CUSTOMER_CANCELED);
        }
        return repairOrderStatusMap.get(code);
    }
}
