package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;

import java.util.Date;

@Data
public class GarageContractRequest {
    private String contractNumber;
    private Long garageId;
    private Date contractFromDate;
    private Date contractToDate;
    private String document;
}
