package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;

import javax.persistence.*;
import java.math.BigDecimal;

@SqlResultSetMapping(
        name = "ConfigPriceDto",
        classes = {
                @ConstructorResult(
                        targetClass = ConfigPriceDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "type", type = Integer.class),
                                @ColumnResult(name = "customer_type_id", type = Long.class),
                                @ColumnResult(name = "customer_type_name", type = String.class),
                                @ColumnResult(name = "product_id", type = Long.class),
                                @ColumnResult(name = "product_name", type = String.class),
                                @ColumnResult(name = "product_code", type = String.class),
                                @ColumnResult(name = "garage_service_id", type = Long.class),
                                @ColumnResult(name = "garage_service_name", type = String.class),
                                @ColumnResult(name = "garage_service_code", type = String.class),
                                @ColumnResult(name = "unit", type = String.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                        })})

@Entity
@Data
@Table(name = "config_prices")
public class ConfigPrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer type;
    private Long customerTypeId;
    private Long productId;
    private Long garageServiceId;
    private BigDecimal price;
    private Long garageId;
}
