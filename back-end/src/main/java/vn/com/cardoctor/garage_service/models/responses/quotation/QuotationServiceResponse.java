package vn.com.cardoctor.garage_service.models.responses.quotation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationServiceResponse {
    private Long serviceId;
    private Integer version;
    private String name;
    private BigDecimal price;
}
