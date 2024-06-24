package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductTypeEnum {
    SPARE_PART(1),
    SERVICE(2);

    private final Integer code;
    private static Map<Integer, ProductTypeEnum> productTypeEnumMap;

    ProductTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ProductTypeEnum getProductTypeEnum(Integer code) {
        if (productTypeEnumMap == null) {
            productTypeEnumMap = new HashMap<>();
            productTypeEnumMap.put(1, ProductTypeEnum.SPARE_PART);
            productTypeEnumMap.put(2, ProductTypeEnum.SERVICE);
        }
        return productTypeEnumMap.get(code);
    }
}
