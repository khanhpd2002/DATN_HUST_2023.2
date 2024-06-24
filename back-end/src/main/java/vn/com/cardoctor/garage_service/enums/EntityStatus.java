package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum EntityStatus {
    ACCEPTED(1),
    REJECTED(2),
    PENDING(3);
    private final Integer code;
    private static Map<Integer, EntityStatus> entityStatusMap;


    EntityStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static EntityStatus getEntityStatus(Integer code) {
        if (entityStatusMap == null) {
            entityStatusMap = new HashMap<>();
            entityStatusMap.put(1, EntityStatus.ACCEPTED);
            entityStatusMap.put(2, EntityStatus.REJECTED);
            entityStatusMap.put(3, EntityStatus.PENDING);
        }
        return entityStatusMap.get(code);
    }
}

