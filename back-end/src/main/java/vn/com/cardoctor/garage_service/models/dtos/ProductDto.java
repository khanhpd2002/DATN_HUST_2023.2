package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private long id;
    private String code;
    private String name;
    private Long parentProductId;
    private Integer sparePartType;
    private Long distributorId;
    private String distributorCode;
    private String distributorName;
    private BigDecimal purchasePrice;
    private BigDecimal quantity;
    private String unit;
}
