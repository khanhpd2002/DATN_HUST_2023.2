package vn.com.cardoctor.garage_service.enums;

import java.util.HashMap;
import java.util.Map;

public enum QuotationStatus {
    DRAFT(0),
    SENT(1),
    CONFIRM(2),
    CANCELED(3);
    private final Integer code;
    private static Map<Integer, QuotationStatus> quotationStatusMap;


    QuotationStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static QuotationStatus getQuotationStatus(Integer code) {
        if (quotationStatusMap == null) {
            quotationStatusMap = new HashMap<>();
            quotationStatusMap.put(1, QuotationStatus.SENT);
            quotationStatusMap.put(2, QuotationStatus.CONFIRM);
            quotationStatusMap.put(3, QuotationStatus.CANCELED);
        }
        return quotationStatusMap.get(code);
    }
}
