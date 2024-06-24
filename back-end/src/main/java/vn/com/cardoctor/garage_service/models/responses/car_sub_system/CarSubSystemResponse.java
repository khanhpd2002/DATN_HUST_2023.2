package vn.com.cardoctor.garage_service.models.responses.car_sub_system;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;

import java.util.List;

@Data
public class CarSubSystemResponse {
    private long id;
    private String name;
    private String description;
    private int status;
}
