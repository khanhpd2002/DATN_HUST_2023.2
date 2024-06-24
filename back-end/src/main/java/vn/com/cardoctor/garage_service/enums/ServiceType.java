package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum ServiceType {

    MAINTENANCE(0),
    SOS(1),
    REPAIR(2),
    UPDATE(3),
    PAINT(4),
    SPA(5);
    private final Integer code;
    private static Map<Integer, ServiceType> serviceTypeMap;


    ServiceType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ServiceType getServiceType(Integer code) {
        if (serviceTypeMap == null) {
            serviceTypeMap = new HashMap<>();
            serviceTypeMap.put(0, ServiceType.MAINTENANCE);
            serviceTypeMap.put(1, ServiceType.SOS);
            serviceTypeMap.put(2, ServiceType.REPAIR);
            serviceTypeMap.put(3, ServiceType.UPDATE);
            serviceTypeMap.put(4, ServiceType.PAINT);
            serviceTypeMap.put(5, ServiceType.SPA);
        }
        return serviceTypeMap.get(code);
    }
}
