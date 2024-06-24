package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "garage_insurances")
@IdClass(GarageInsuranceId.class)
@Data
public class GarageInsurance implements Serializable {
    @Id
    private Long garageId;

    @Id
    private Long insuranceId;
}
