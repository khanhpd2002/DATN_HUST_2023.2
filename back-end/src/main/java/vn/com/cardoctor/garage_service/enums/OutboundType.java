package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum OutboundType {
    FOR_REFUND(1),
    FOR_REPAIR_ORDER(2),
    FOR_SELL_SPARE_PART(3);
    private final Integer code;
    private static Map<Integer, OutboundType> outboundTypeMap;


    OutboundType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static OutboundType getOutboundType(Integer code) {
        if (outboundTypeMap == null) {
            outboundTypeMap = new HashMap<>();
            outboundTypeMap.put(1, OutboundType.FOR_REFUND);
            outboundTypeMap.put(2, OutboundType.FOR_REPAIR_ORDER);
            outboundTypeMap.put(3, OutboundType.FOR_SELL_SPARE_PART);
        }
        return outboundTypeMap.get(code);
    }
}
