package vn.com.cardoctor.garage_service.models.requests.diagnose;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SparePartInfo {
    private Long versionId;
    private Long diagnoseId;
    private String serialNumber;
    private String code;
    private BigDecimal unitPrice;
    private Integer quantity;
    private String unit;
}
