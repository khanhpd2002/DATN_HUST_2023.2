package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum PaymentCompletedStatus {
    WAIT_FOR_PAY(1),
    DEPOSIT(2),
    PAID(3),
    REFUNDED(4),
    OVERDUE(5);

    private final Integer code;
    private static Map<Integer, PaymentCompletedStatus> paymentCompletedStatusMap;

    PaymentCompletedStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static PaymentCompletedStatus getPaymentCompletedStatus(Integer code) {
        if (paymentCompletedStatusMap == null) {
            paymentCompletedStatusMap = new HashMap<>();
            paymentCompletedStatusMap.put(1, PaymentCompletedStatus.WAIT_FOR_PAY);
            paymentCompletedStatusMap.put(2, PaymentCompletedStatus.DEPOSIT);
            paymentCompletedStatusMap.put(3, PaymentCompletedStatus.PAID);
            paymentCompletedStatusMap.put(4, PaymentCompletedStatus.REFUNDED);
            paymentCompletedStatusMap.put(5, PaymentCompletedStatus.OVERDUE);
        }
        return paymentCompletedStatusMap.get(code);
    }
}
