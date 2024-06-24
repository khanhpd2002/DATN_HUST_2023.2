package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum DeliverySparePartStatus {
    RECEIVE_ORDER(1),
    ORDER_SUCCESS(2),
    ON_THE_WAY(3),
    RECEIVE_PRODUCT(4),
    REFUNDED(5),
    CANCELLED(6);

    private final Integer code;
    private static Map<Integer, DeliverySparePartStatus> deliverySparePartStatusMap;

    DeliverySparePartStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static DeliverySparePartStatus getDeliverySparePartStatus(Integer code) {
        if (deliverySparePartStatusMap == null) {
            deliverySparePartStatusMap = new HashMap<>();
            deliverySparePartStatusMap.put(1, DeliverySparePartStatus.RECEIVE_ORDER);
            deliverySparePartStatusMap.put(2, DeliverySparePartStatus.ORDER_SUCCESS);
            deliverySparePartStatusMap.put(3, DeliverySparePartStatus.ON_THE_WAY);
            deliverySparePartStatusMap.put(4, DeliverySparePartStatus.RECEIVE_PRODUCT);
            deliverySparePartStatusMap.put(5, DeliverySparePartStatus.REFUNDED);
            deliverySparePartStatusMap.put(6, DeliverySparePartStatus.CANCELLED);
        }
        return deliverySparePartStatusMap.get(code);
    }
}
