package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum OfferTypeEnum {
    GARAGE(1),
    ACCESSORY(2);

    private final Integer code;
    private static Map<Integer, OfferTypeEnum> offerTypeEnumMap;

    OfferTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static OfferTypeEnum getOfferTypeEnum(Integer code) {
        if (offerTypeEnumMap == null) {
            offerTypeEnumMap = new HashMap<>();
            offerTypeEnumMap.put(1, OfferTypeEnum.GARAGE);
            offerTypeEnumMap.put(2, OfferTypeEnum.ACCESSORY);
        }
        return offerTypeEnumMap.get(code);
    }

}
