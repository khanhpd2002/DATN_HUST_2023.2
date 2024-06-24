package vn.com.cardoctor.garage_service.models.responses.e_service;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;
import vn.com.cardoctor.garage_service.entities.garages.Tag;

import java.util.List;

@Data
public class EServiceResponse {
    private Long id;
    private String name;
    private String description;
    private int isDefault;
    private int status;
    private List<CarSubSystem> carSubSystems;
    private List<Tag> tags;
}
