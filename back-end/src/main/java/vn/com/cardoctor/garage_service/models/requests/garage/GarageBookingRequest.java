package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GarageBookingRequest {
    private List<Long> serviceIds;
    private Long maxDistance;
    private BigDecimal currentLat;
    private BigDecimal currentLng;
}
