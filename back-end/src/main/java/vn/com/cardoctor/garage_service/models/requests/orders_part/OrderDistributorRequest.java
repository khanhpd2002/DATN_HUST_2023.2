package vn.com.cardoctor.garage_service.models.requests.orders_part;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDistributorRequest {
    private Long distributorId;
    private String distributorCode;
    private String distributorName;
    private String distributorPhone;
    private String deliveryType;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private Long inventoryId;
    private List<OrderDistributorProductRequest> products;
    private List<OrderDistributorProductRequest> addProducts;
    private List<OrderDistributorProductRequest> removeProducts;
}
