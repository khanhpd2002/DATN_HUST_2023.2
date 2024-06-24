package vn.com.cardoctor.garage_service.models.responses.diagnose;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SparePartInfoResponse {
    private String serialNumber;
    private String code;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String unit;
}
