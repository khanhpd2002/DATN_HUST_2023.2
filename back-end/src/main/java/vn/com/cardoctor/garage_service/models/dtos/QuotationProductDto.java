package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationProductDto {
    private Long quotationId;
    private Long garageServiceId;
    private Long garageServiceTypeId;
    private String garageServiceTypeName;
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
    private Long employeeId;
    private String employeeName;
    private Integer productType;
    private Integer status;
    private Long outboundProductId;
}
