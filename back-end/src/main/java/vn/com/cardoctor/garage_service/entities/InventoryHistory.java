package vn.com.cardoctor.garage_service.entities;

import lombok.Data;
import org.hibernate.annotations.Type;
import vn.com.cardoctor.garage_service.models.dtos.InventoryHistoryDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(
        name = "InventoryHistoryDto",
        classes = {
                @ConstructorResult(
                        targetClass = InventoryHistoryDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "counter_name", type = String.class),
                                @ColumnResult(name = "count_date", type = Date.class),
                                @ColumnResult(name = "error_rate", type = BigDecimal.class),
                                @ColumnResult(name = "inventory_id", type = Long.class),
                        })})

@Entity
@Data
@Table(name = "inventory_histories")
public class InventoryHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String countersName;
    @Temporal(TemporalType.DATE)
    private Date countDate;
    @Temporal(TemporalType.DATE)
    private Date adjustmentDate;
    private BigDecimal errorRate;
    private Long inventoryId;
    private String approvers;
    private Boolean canAdjustment;
}
