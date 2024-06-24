package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellSparePartDto {
    private Long id;
    private String sellCode;
    private String customerName;
    private Date createdAt;
    private BigDecimal totalPrice;
    private Integer deliveryStatus;
    private Integer paymentStatus;
}
