package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;

import java.util.Date;

@Data
public class GarageGroupRequest {
    private String name;
    private String code;
    private String phone;
    private String website;
    private String email;
    private String taxCode;
    private String description;
    private String insurancePartner;
    private String managementExp;
    private String placeOfIssue;
    private Date dateOfIssue;
    private String address;
    private Integer type;
}
