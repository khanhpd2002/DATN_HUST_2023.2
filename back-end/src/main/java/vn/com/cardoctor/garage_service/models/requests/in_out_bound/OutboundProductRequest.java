package vn.com.cardoctor.garage_service.models.requests.in_out_bound;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutboundProductRequest {
    private Long productId;
    private String unit;
    private BigDecimal requestQuantity;
    private BigDecimal exportQuantity;
    private String note;
}
