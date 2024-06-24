package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "adjustment_inventory_quantity_histories")
public class AdjustmentInventoryQuantityHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
}
