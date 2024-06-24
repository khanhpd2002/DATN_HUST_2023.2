package vn.com.cardoctor.garage_service.entities.orders_part;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "log_order_distributor")
public class LogOrderDistributor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderDistributorId;
    private Integer logVersion;
    private String products;
    private String actionUser;
}
