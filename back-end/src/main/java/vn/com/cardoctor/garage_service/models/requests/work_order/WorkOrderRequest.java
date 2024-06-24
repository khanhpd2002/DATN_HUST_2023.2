package vn.com.cardoctor.garage_service.models.requests.work_order;

import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class WorkOrderRequest {
    private Date scheduledDate;
    private Date plannedEndDate;
    private LocalTime plannedDuration;
    private Date actualStartDate;
    private Date actualEndDate;
    private Date actualDuration;
    private Integer status;
}
