package vn.com.cardoctor.garage_service.models.requests.repair_order;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationLabourRequest;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationSparePartRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RepairOrderGarageRequest {
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long customerTypeId;
    private Long carId;
    private String vinNumber;
    private String licensePlate;
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
    private List<QuotationLabourRequest> quotationLabours;
    private List<QuotationSparePartRequest> quotationSpareParts;
    private BigDecimal discount;
    private BigDecimal totalPrice;
    private String description;
    private Integer odo;
    private Long garageId;
    private Date appointmentDate;
    private Integer status;
    private Integer paymentStatus;
}
