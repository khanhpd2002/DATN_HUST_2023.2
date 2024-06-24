package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum InboundType {
    ORDER_DISTRIBUTOR(1),
    REFUND_REPAIR_ORDER(2),
    REFUND_SELL_TICKET(3);
    private final Integer code;
    private static Map<Integer, InboundType> inboundTypeMap;


    InboundType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static InboundType getInboundType(Integer code) {
        if (inboundTypeMap == null) {
            inboundTypeMap = new HashMap<>();
            inboundTypeMap.put(1, InboundType.ORDER_DISTRIBUTOR);
            inboundTypeMap.put(2, InboundType.REFUND_REPAIR_ORDER);
            inboundTypeMap.put(3, InboundType.REFUND_SELL_TICKET);
        }
        return inboundTypeMap.get(code);
    }
}
