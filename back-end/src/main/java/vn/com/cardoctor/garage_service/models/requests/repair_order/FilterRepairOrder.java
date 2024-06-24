package vn.com.cardoctor.garage_service.models.requests.repair_order;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FilterRepairOrder {
    private Long garageId;
    private Date fromDate;
    private Date toDate;
    private Long status;
    private Long paymentStatus;
    private Long customerTypeId;
    private String customerName;
    private String filter;
    private Integer pageNumber;
    private Integer pageSize;
}
