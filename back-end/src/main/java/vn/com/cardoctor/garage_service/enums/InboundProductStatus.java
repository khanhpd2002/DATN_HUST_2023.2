package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum InboundProductStatus {
    DA_NHAP(1),
    CHUA_NHAP(2);
    private final Integer code;
    private static Map<Integer, InboundProductStatus> inboundProductStatusMap;


    InboundProductStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static InboundProductStatus getInboundProductStatus(Integer code) {
        if (inboundProductStatusMap == null) {
            inboundProductStatusMap = new HashMap<>();
            inboundProductStatusMap.put(1, InboundProductStatus.DA_NHAP);
            inboundProductStatusMap.put(2, InboundProductStatus.CHUA_NHAP);
        }
        return inboundProductStatusMap.get(code);
    }
}
