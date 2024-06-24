package vn.com.cardoctor.garage_service.models.requests.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationSparePartRequest {
    private Long productId;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal price;
}
