package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDistributorDto {
    private Long id;
    private String orderCode;
    private String distributorName;
    private Date createdAt;
    private BigDecimal totalPrice;
    private Integer deliveryStatus;
    private Integer paymentStatus;
}
