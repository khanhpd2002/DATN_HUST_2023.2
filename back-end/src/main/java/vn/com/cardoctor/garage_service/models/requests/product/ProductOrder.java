package vn.com.cardoctor.garage_service.models.requests.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductOrder {
    private long productId;
    private BigDecimal quantity;
}
