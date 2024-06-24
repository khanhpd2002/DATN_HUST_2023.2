package vn.com.cardoctor.garage_service.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "customer_types")
public class CustomerType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String customerTypeName;
    private String description;
    private Long garageId;
}
