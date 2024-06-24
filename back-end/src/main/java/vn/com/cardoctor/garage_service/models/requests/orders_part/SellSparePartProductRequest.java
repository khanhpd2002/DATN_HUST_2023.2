package vn.com.cardoctor.garage_service.models.requests.orders_part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellSparePartProductRequest {
    private Long productId;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private Integer status;
    private Long outboundProductId;
}
