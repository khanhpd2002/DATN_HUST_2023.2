package vn.com.cardoctor.garage_service.models.requests.quotation;

import lombok.Data;

import java.util.List;

@Data
public class QuotationGmsRequest {
    private String note;
    private List<QuotationLabourRequest> quotationLabours;
    private List<QuotationSparePartGmsRequest> addQuotationSpareParts;
    private List<QuotationSparePartGmsRequest> removeQuotationSpareParts;
    private List<QuotationSparePartGmsRequest> quotationSpareParts;
}
