package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistoryDto {
    private long id;
    private String code;
    private String name;
    private String unit;
    private BigDecimal systemUnitPrice;
    private BigDecimal realityUnitPrice;
    private BigDecimal quantity;
}
