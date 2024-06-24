package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "service_car_sub_systems")
@IdClass(ServiceCarSubSystemId.class)
public class ServiceCarSubSystem implements Serializable {
    @Id
    private Long serviceId;

    @Id
    private Long carSubSystemId;
}
