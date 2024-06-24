package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private Long customerTypeId;
    private String customerTypeName;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long garageId;
    private Long driverId;
}
