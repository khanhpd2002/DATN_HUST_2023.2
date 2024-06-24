package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum GaragePromoteEnum {
    PAID(1),
    NEW(2),
    TOPBOOK(3),
    SPECIAL(4);


    private final Integer code;
    private static Map<Integer, GaragePromoteEnum> garagePromoteEnumMap;

    GaragePromoteEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static GaragePromoteEnum getGaragePromoteEnum(Integer code) {
        if (garagePromoteEnumMap == null) {
            garagePromoteEnumMap = new HashMap<>();
            garagePromoteEnumMap.put(1, GaragePromoteEnum.PAID);
            garagePromoteEnumMap.put(2, GaragePromoteEnum.NEW);
            garagePromoteEnumMap.put(3, GaragePromoteEnum.TOPBOOK);
            garagePromoteEnumMap.put(4, GaragePromoteEnum.SPECIAL);
        }
        return garagePromoteEnumMap.get(code);
    }
}
