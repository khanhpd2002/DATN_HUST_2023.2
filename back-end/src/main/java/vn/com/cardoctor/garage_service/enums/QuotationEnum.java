package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum QuotationEnum {
    BY_PRODUCT_SERVICE(1),
    BY_IMAGE(2);
    private final Integer code;
    private static Map<Integer, QuotationEnum> quotationEnumMap;


    QuotationEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static QuotationEnum getQuotationEnum(Integer code) {
        if (quotationEnumMap == null) {
            quotationEnumMap = new HashMap<>();
            quotationEnumMap.put(1, QuotationEnum.BY_PRODUCT_SERVICE);
            quotationEnumMap.put(2, QuotationEnum.BY_IMAGE);
        }
        return quotationEnumMap.get(code);
    }
}
