package vn.com.cardoctor.garage_service.models.requests.repair_order;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.EService;
import vn.com.cardoctor.garage_service.models.requests.Image;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class RepairOrderRequest {
    private Long garageId;
    // Customer info
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long customerTypeId;
    // Car info
    private Long carId;
    private String vinNumber;
    private String licensePlate;
    private String carName;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;

    private String description;
    private List<EService> services;
    private Integer status;
    private Integer priority;
    private Date appointmentDate;
    private LocalDateTime expectedHandoverDate;
    private Integer timeFrame;
    private Integer odo;
    private Long expertId;
    private Long operatorId;
    private List<Image> images;
}
