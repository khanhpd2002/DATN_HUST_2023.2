package vn.com.cardoctor.garage_service.models.requests.customer;

import lombok.Data;

@Data
public class CustomerTypeRequest {
    private String customerTypeName;
    private String description;
}
