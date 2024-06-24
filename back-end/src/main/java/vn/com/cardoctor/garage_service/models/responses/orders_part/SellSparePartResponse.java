package vn.com.cardoctor.garage_service.models.responses.orders_part;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellSparePartResponse {
    private Long id;
    private String sellCode;
    private Long customerId;
    private Integer deliveryStatus;
    private Integer paymentStatus;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private Long garageId;
    private Long inventoryId;
    private List<SellSparePartProductResponse> products;
    private Integer discountType;
}
