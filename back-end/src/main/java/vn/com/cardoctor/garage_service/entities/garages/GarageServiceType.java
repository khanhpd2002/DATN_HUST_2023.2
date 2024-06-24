package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "garage_service_types")
@Data
public class GarageServiceType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long garageId;
}
