package vn.com.cardoctor.garage_service.models.requests.quotation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationServiceRequest {
    private Long serviceId;
    private Integer version;
    private String name;
    private BigDecimal price;
}