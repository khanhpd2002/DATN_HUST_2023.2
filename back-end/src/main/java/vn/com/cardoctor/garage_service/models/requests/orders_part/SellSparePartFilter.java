package vn.com.cardoctor.garage_service.models.requests.orders_part;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SellSparePartFilter {
    private Long customerTypeId;
    private String customerPhone;
    private String customerName;
    private Date fromDate;
    private Date toDate;
    private String filter;
    private Integer deliveryStatus;
    private Integer paymentStatus;
}
