package vn.com.cardoctor.garage_service.models.responses.garage_owner;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;

import java.util.List;

@Data
public class GarageOwnerResponse {
    List<GarageOwner> data;
}
