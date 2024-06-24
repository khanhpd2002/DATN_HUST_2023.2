package vn.com.cardoctor.garage_service.entities.orders_part;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "log_sell_spare_part")
public class LogSellSparePart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellSparePartId;
    private Integer logVersion;
    private String products;
    private String actionUser;
}
