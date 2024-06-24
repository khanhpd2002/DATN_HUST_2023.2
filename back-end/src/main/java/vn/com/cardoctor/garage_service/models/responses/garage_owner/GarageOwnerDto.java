package vn.com.cardoctor.garage_service.models.responses.garage_owner;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.Garage;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class GarageOwnerDto {
    private long id;
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
    private String keycloakId;
    private Integer status;
    private List<Garage> garages;
}
