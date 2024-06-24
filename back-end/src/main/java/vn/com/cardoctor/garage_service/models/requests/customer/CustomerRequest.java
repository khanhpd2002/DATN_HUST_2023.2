package vn.com.cardoctor.garage_service.models.requests.customer;

import lombok.Data;

@Data
public class CustomerRequest {
    private Long customerTypeId;
    private String customerTypeName;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long garageId;
    private Long driverId;
}
