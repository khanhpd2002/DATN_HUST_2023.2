package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationLabourResponse {
    private Long quotationId;
    private Long garageServiceId;
    private Long garageServiceTypeId;
    private String garageServiceTypeName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String unit;
    private BigDecimal originPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal price;
    private Long employeeId;
    private String employeeName;
    private Long outboundProductId;
}
