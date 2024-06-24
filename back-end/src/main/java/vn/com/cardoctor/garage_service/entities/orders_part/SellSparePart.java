package vn.com.cardoctor.garage_service.entities.orders_part;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;
import vn.com.cardoctor.garage_service.models.dtos.SellSparePartDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(
        name = "SellSparePartDto",
        classes = {
                @ConstructorResult(
                        targetClass = SellSparePartDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "sell_code", type = String.class),
                                @ColumnResult(name = "customer_name", type = String.class),
                                @ColumnResult(name = "created_at", type = Date.class),
                                @ColumnResult(name = "total_price", type = BigDecimal.class),
                                @ColumnResult(name = "delivery_status", type = Integer.class),
                                @ColumnResult(name = "payment_status", type = Integer.class),
                        })})

@Entity
@Data
@Table(name = "sell_spare_parts")
public class SellSparePart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sellCode;
    private Long customerId;
    private Integer deliveryStatus;
    private Integer paymentStatus;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal totalPrice;
    private String products;
    private Long garageId;
    private Long inventoryId;
    private Integer discountType;
}
