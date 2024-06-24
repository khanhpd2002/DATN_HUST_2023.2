package vn.com.cardoctor.garage_service.models.requests.repair_order;

import lombok.Data;

@Data
public class CarInfo {
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String vinNumber;
    private String licensePlate;
    private Long driverCarId;
}
