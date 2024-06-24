package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.GarageServiceDto;

import javax.persistence.*;

@SqlResultSetMapping(
        name = "GarageServiceDto",
        classes = {
                @ConstructorResult(
                        targetClass = GarageServiceDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "code", type = String.class),
                                @ColumnResult(name = "description", type = String.class),
                                @ColumnResult(name = "garage_service_type", type = String.class),
                                @ColumnResult(name = "garage_id", type = Long.class),
                        })})

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "garage_services")
@Data
public class GarageService extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private Long garageServiceTypeId;
    private Long garageId;
    private Boolean isActive;
}
