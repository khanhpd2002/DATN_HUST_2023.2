package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Garage;

import java.util.Date;

@Data
public class GarageContractResponse {
    private long id;
    private String contractNumber;
    private Long garageId;
    private Garage garage;
    private Date contractFromDate;
    private Date contractToDate;
    private String document;
}
