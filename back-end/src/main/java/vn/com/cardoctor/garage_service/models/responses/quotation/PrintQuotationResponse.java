package vn.com.cardoctor.garage_service.models.responses.quotation;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationLabourResponse;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationSparePartResponse;

import java.util.List;
import java.util.Map;

@Data
public class PrintQuotationResponse {
    private Long id;
    private Long repairOrderId;
    private Long diagnoseId;
    private Integer status;
    private List<QuotationLabourResponse> quotationLabours;
    private List<QuotationSparePartResponse> quotationSpareParts;
    private List<QuotationSparePartResponse> oldQuotationSpareParts;
    private Map<String, List<QuotationLabourResponse>> printQuotationLabours;
}
