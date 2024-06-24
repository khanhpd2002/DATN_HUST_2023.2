package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum InOutboundStatus {
    DELETED(0),
    VERIFIED(1),
    UN_VERIFIED(2);
    private final Integer code;
    private static Map<Integer, InOutboundStatus> inboundStatusMap;


    InOutboundStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static InOutboundStatus getInboundStatus(Integer code) {
        if (inboundStatusMap == null) {
            inboundStatusMap = new HashMap<>();
            inboundStatusMap.put(0, InOutboundStatus.DELETED);
            inboundStatusMap.put(1, InOutboundStatus.VERIFIED);
            inboundStatusMap.put(2, InOutboundStatus.UN_VERIFIED);
        }
        return inboundStatusMap.get(code);
    }
}
