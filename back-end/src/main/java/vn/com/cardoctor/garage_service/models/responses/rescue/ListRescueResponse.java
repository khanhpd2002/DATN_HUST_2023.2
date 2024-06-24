package vn.com.cardoctor.garage_service.models.responses.rescue;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;

import java.util.List;

@Data
public class ListRescueResponse {
    private List<Rescue> data;
}
