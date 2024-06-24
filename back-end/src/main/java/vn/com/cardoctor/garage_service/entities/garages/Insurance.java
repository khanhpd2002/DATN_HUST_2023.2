package vn.com.cardoctor.garage_service.entities.garages;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "insurances")
public class Insurance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer status;
}
