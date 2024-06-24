package vn.com.cardoctor.garage_service.models.requests.repair_order;

import lombok.Data;

@Data
public class CustomerInfo {
    private String fullName;
    private String phoneNumber;
    private Long driverId;
}
