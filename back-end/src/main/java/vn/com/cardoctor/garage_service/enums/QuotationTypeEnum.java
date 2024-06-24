package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum QuotationTypeEnum {
    SPARE_PART(1),
    SERVICE(2);

    private final Integer code;
    private static Map<Integer, QuotationTypeEnum> quotationTypeEnumMap;


    QuotationTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static QuotationTypeEnum getQuotationTypeEnum(Integer code) {
        if (quotationTypeEnumMap == null) {
            quotationTypeEnumMap = new HashMap<>();
            quotationTypeEnumMap.put(1, QuotationTypeEnum.SPARE_PART);
            quotationTypeEnumMap.put(2, QuotationTypeEnum.SERVICE);
        }
        return quotationTypeEnumMap.get(code);
    }
}
