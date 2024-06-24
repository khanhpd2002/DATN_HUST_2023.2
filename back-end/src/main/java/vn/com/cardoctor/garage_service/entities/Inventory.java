package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "inventories")
public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Long garageId;
}
