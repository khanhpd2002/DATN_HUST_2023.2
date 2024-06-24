package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum PaymentStatusEnum {
    NOT_PAY(1),
    PAID(2);

    private final Integer code;
    private static Map<Integer, PaymentStatusEnum> paymentStatusEnumMap;

    PaymentStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
    public static PaymentStatusEnum getPaymentStatusEnum(Integer code) {
        if (paymentStatusEnumMap == null) {
            paymentStatusEnumMap = new HashMap<>();
            paymentStatusEnumMap.put(1, PaymentStatusEnum.NOT_PAY);
            paymentStatusEnumMap.put(2, PaymentStatusEnum.PAID);
        }
        return paymentStatusEnumMap.get(code);
    }
}
