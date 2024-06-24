package vn.com.cardoctor.garage_service.models.responses.garage;

import lombok.Data;

import java.util.List;

@Data
public class ListGarageResponse {
    List<GarageResponse> data;
}
