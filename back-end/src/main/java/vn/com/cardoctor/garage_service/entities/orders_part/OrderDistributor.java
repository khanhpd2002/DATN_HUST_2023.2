package vn.com.cardoctor.garage_service.entities.orders_part;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.OrderDistributorDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@SqlResultSetMapping(
        name = "OrderDistributorDto",
        classes = {
                @ConstructorResult(
                        targetClass = OrderDistributorDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "order_code", type = String.class),
                                @ColumnResult(name = "distributor_name", type = String.class),
                                @ColumnResult(name = "created_at", type = Date.class),
                                @ColumnResult(name = "total_price", type = BigDecimal.class),
                                @ColumnResult(name = "delivery_status", type = Integer.class),
                                @ColumnResult(name = "payment_status", type = Integer.class),
                        })})

@Entity
@Data
@Table(name = "orders_distributor")
public class OrderDistributor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderCode;
    private Long distributorId;
    private String deliveryType;
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
