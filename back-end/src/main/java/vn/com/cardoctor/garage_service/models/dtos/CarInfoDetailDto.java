package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarInfoDetailDto {

    private long id;
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private String licensePlate;
    private String vinNumber;
    private Long customerId;
    private Long garageId;
    private Long driverCarId;
    List<HistoryRepairOrderDto> listHistoryRepairOrders;
}
