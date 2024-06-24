package vn.com.cardoctor.garage_service.models.responses.garage;

import lombok.Data;

@Data
public class GarageServiceResponse {
    private Long garageServiceId;
    private Long garageServiceTypeId;
    private Integer instanceKey;
}
