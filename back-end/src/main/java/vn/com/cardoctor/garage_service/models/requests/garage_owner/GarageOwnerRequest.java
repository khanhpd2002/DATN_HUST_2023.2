package vn.com.cardoctor.garage_service.models.requests.garage_owner;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Garage;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
public class GarageOwnerRequest {
    private String name;
    private String code;
    private String userName;
    private String phone;
    private String email;
    private String identifiedCard;
    private Date dateOfIssue;
    private String placeOfIssue;
    private Integer gender;
    private Date birthday;
    private String avatar;
    private List<Garage> garages;
}
