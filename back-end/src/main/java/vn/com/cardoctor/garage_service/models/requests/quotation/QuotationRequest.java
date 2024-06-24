package vn.com.cardoctor.garage_service.models.requests.quotation;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.requests.Image;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuotationRequest {
    private List<QuotationLabourRequest> quotationLabours;
    private List<QuotationSparePartRequest> quotationSpareParts;
    private BigDecimal totalPrice;
    private List<Image> images;
    private LocalDateTime expectedHandoverDate;
}
