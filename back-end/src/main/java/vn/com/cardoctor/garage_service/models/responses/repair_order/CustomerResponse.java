package vn.com.cardoctor.garage_service.models.responses.repair_order;

import lombok.Data;

@Data
public class CustomerResponse {
    private String fullName;
    private String phoneNumber;
    private Long customerTypeId;
    private Long driverId;
    private String address;
}
