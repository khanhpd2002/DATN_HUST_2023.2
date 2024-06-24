package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.ProductCompatibility;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponse {
    private long id;
    private String code;
    private String name;
    private String color;
    private Integer sparePartType;
    private Long distributorId;
    private BigDecimal purchasePrice;
    private BigDecimal quantity;
    private String unit;
    private String description;
    private Long inventoryId;
    private Long garageId;
    private List<ProductCompatibility> productCompatibilities;
}
