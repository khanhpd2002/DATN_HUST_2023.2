package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Data
@Table(name = "car_sub_systems")
public class CarSubSystem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private int status;
}
