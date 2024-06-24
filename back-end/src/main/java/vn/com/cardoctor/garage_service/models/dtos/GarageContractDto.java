package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageContractDto {
    private long id;
    private String contractNumber;
    private Long garageId;
    private Date contractFromDate;
    private Date contractToDate;
    private String document;
    private Integer status;
    private String garageName;
}
