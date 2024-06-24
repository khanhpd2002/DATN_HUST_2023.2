package vn.com.cardoctor.garage_service.models.responses.repair_order;

import lombok.Data;

@Data
public class CarResponse {
    private Long carId;
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String licensePlate;
    private String vinNumber;
    private Long driverCarId;
}
