package vn.com.cardoctor.garage_service.models.requests.quotation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationSparePartGmsRequest {
    private Long productId;
    private String productName;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal price;
    private BigDecimal originPrice;
    private Integer status;
    private Long outboundProductId;
    private Long outboundTicketId;
}
