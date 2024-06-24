package vn.com.cardoctor.garage_service.models.responses.garage;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.Insurance;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;
import vn.com.cardoctor.garage_service.models.dtos.CustomFieldDto;
import vn.com.cardoctor.garage_service.models.requests.Image;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GarageResponse {
    private Garage garage;
    private List<CarSubSystem> carSubSystems;
    private List<Rescue> rescues;
    private List<Insurance> insurances;
    private List<CustomFieldDto> customFields;
}
