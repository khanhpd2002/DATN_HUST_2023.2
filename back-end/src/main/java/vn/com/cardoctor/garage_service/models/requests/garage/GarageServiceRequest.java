package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;

@Data
public class GarageServiceRequest {
    private String name;
    private String code;
    private String description;
    private Long garageServiceTypeId;
    private String garageServiceTypeName;
    private Boolean isActive;
    private Integer instanceKey;
}
