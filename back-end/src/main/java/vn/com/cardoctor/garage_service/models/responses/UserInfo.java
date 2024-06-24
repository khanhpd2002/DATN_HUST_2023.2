package vn.com.cardoctor.garage_service.models.responses;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;

import java.util.List;

@Data
public class UserInfo {
    private GarageOwner garageOwner;
    private List<Garage> garages;
}
