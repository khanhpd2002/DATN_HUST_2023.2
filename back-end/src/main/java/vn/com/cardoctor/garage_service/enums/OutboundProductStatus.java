package vn.com.cardoctor.garage_service.enums;


import java.util.HashMap;
import java.util.Map;

public enum OutboundProductStatus {
    DA_XUAT(1),
    CHUA_XUAT(2);
    private final Integer code;
    private static Map<Integer, OutboundProductStatus> outboundProductStatusMap;


    OutboundProductStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static OutboundProductStatus getOutboundProductStatus(Integer code) {
        if (outboundProductStatusMap == null) {
            outboundProductStatusMap = new HashMap<>();
            outboundProductStatusMap.put(1, OutboundProductStatus.DA_XUAT);
            outboundProductStatusMap.put(2, OutboundProductStatus.CHUA_XUAT);
        }
        return outboundProductStatusMap.get(code);
    }
}

