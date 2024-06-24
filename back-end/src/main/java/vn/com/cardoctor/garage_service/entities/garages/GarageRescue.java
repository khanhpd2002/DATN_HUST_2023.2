package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "garage_rescues")
@IdClass(GarageRescueId.class)
@Data
public class GarageRescue implements Serializable {
    @Id
    private long garageId;

    @Id
    private long rescueId;
}
