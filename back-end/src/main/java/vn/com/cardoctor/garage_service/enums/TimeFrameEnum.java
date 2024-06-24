package vn.com.cardoctor.garage_service.enums;

import java.util.Map;

public enum TimeFrameEnum {
    MORNING(1),
    AFTERNOON(2);

    private final Integer code;

    private static Map<Integer, TimeFrameEnum> timeFrameEnumMap;

    TimeFrameEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static TimeFrameEnum getTimeFrameEnum(Integer code) {
        if (timeFrameEnumMap == null) {
            timeFrameEnumMap.put(1, TimeFrameEnum.MORNING);
            timeFrameEnumMap.put(2, TimeFrameEnum.AFTERNOON);
        }
        return timeFrameEnumMap.get(code);
    }
}
