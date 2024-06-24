package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "garage_offers")
@IdClass(GarageOfferId.class)
public class GarageOffer implements Serializable {
    @Id
    private long garageId;

    @Id
    private long offerId;
}
