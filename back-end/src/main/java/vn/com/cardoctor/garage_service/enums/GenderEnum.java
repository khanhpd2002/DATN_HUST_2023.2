package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum GenderEnum {
    MALE(0),
    FEMALE(1),
    UNKNOW(2);
    private final Integer code;
    private static Map<Integer, GenderEnum> genderEnumMap;


    GenderEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static GenderEnum getGenderEnum(Integer code) {
        if (genderEnumMap == null) {
            genderEnumMap = new HashMap<>();
            genderEnumMap.put(0, GenderEnum.MALE);
            genderEnumMap.put(1, GenderEnum.FEMALE);
            genderEnumMap.put(2, GenderEnum.UNKNOW);
        }
        return genderEnumMap.get(code);
    }
}


