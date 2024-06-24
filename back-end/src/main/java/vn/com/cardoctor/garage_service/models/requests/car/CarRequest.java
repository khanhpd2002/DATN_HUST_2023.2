package vn.com.cardoctor.garage_service.models.requests.car;

import lombok.Data;

@Data
public class CarRequest {
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String licensePlate;
    private String vinNumber;
    private Long driverCarId;
    private Long customerId;
}
