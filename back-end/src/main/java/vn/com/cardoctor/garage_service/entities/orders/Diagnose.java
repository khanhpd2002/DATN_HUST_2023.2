package vn.com.cardoctor.garage_service.entities.orders;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Data
@Table(name = "diagnoses")
public class Diagnose extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long repairOrderId;
    private Long employeeId;
    private String description;
    private String images;
}
