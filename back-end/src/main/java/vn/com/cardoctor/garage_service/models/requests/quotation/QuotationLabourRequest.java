package vn.com.cardoctor.garage_service.models.requests.quotation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationLabourRequest {
    private Long garageServiceTypeId;
    private Long garageServiceId;
    private String garageServiceName;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal price;
    private Long employeeId;
}
