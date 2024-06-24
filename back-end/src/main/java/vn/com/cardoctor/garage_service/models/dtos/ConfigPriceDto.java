package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPriceDto {
    private long id;
    private Integer type;
    private Long customerTypeId;
    private String customerTypeName;
    private Long productId;
    private String productName;
    private String productCode;
    private Long garageServiceId;
    private String garageServiceName;
    private String garageServiceCode;
    private String unit;
    private BigDecimal price;
    private Long garageId;
}
