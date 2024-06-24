package vn.com.cardoctor.garage_service.models.responses.garage;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GarageInfoResponse {
    private Long id;
    private String contractPointPhone;
    private String contactPointName;
    private String name;
    private String address;
    private String contactPointEmail;
    private String taxCode;
    private String logo;
    private Map<String, Object> additionalInformation = new HashMap<>();
}
