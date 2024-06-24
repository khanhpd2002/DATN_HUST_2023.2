package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationSparePartResponse {
    private Long quotationId;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String unit;
    private BigDecimal originPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal price;
    private Integer status;
    private Long outboundProductId;
    private Long outboundTicketId;
}
