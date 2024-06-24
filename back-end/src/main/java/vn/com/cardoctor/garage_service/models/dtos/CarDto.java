package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private long id;
    private String carName;
    private Long carBrandId;
    private String carBrandName;
    private Long carModelId;
    private String carModelName;
    private Long carYearId;
    private String carYearName;
    private Long carVersionId;
    private String carVersionName;
    private Long customerId;
    private String licensePlate;
    private String vinNumber;
    private String customerName;
    private String customerPhone;
    private String customerTypeName;
    private Long garageId;
}
