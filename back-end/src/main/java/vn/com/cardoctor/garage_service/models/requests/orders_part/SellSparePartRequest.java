package vn.com.cardoctor.garage_service.models.requests.orders_part;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SellSparePartRequest {
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long customerTypeId;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private Long inventoryId;
    private List<SellSparePartProductRequest> products;
    private List<SellSparePartProductRequest> addProducts;
    private List<SellSparePartProductRequest> removeProducts;
}
