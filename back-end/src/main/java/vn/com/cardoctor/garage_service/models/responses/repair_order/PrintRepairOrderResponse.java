package vn.com.cardoctor.garage_service.models.responses.repair_order;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.requests.Image;
import vn.com.cardoctor.garage_service.models.responses.diagnose.DiagnoseResponse;
import vn.com.cardoctor.garage_service.models.responses.quotation.PrintQuotationResponse;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class PrintRepairOrderResponse {
    private CarResponse carResponse;
    private CustomerResponse customerResponse;
    private String code;
    private String description;
    private List<Image> images;
    private Date appointmentDate;
    private LocalDateTime expectedHandoverDate;
    private Integer odo;
    private Integer timeFrame;
    private Integer priority;
    private Integer status;
    private Integer paymentStatus;
    private Date canceledAt;
    private Long expertId;
    private Long operatorId;
    private Long garageId;
    private String note;
    private DiagnoseResponse diagnose;
    private PrintQuotationResponse quotation;
}
