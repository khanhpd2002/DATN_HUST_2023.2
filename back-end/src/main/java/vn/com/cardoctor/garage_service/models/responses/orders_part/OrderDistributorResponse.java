package vn.com.cardoctor.garage_service.models.responses.orders_part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDistributorResponse {
    private Long id;
    private String orderCode;
    private Long distributorId;
    private String deliveryType;
    private Integer deliveryStatus;
    private Integer paymentStatus;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private Long garageId;
    private Long inventoryId;
    private List<OrderDistributorProductResponse> products;
    private Integer discountType;
}
