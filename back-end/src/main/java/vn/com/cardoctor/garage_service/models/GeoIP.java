package vn.com.cardoctor.garage_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoIP {
    private String ipAddress;
    private String city;
    private String latitude;
    private String longitude;
    // constructors, getters and setters...
}