package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConfigPriceCustomerType {
    private Long modelId;
    private Long customerTypeId;
    private String customerTypeName;
    private BigDecimal price;
}
