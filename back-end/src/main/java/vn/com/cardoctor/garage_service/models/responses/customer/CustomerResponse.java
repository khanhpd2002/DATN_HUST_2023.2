package vn.com.cardoctor.garage_service.models.responses.customer;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.Car;
import vn.com.cardoctor.garage_service.models.dtos.CarDto;

import java.util.List;

@Data
public class CustomerResponse {
    private long id;
    private Long customerTypeId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long garageId;
    private Long driverId;
    private List<CarDto> cars;
}
