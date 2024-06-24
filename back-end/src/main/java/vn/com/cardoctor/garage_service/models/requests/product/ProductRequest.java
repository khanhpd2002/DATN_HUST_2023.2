package vn.com.cardoctor.garage_service.models.requests.product;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String code;
    private String name;
    private Integer sparePartType;
    private Long distributorId;
    private BigDecimal purchasePrice;
    private String unit;
    private BigDecimal quantity;
    private List<ProductCompatibilityRequest> productCompatibilities;

    @Transient
    private String errors;
}
