package vn.com.cardoctor.garage_service.models.responses.garage;

import lombok.Data;

import java.util.Date;

@Data
public class GarageGroupResponse {
    private Long id;
    private String code;
    private String name;
    private String garageOwnerName;
    private String garageOwnerPhone;
    private String website;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address;
    private String taxCode;
    private String placeOfIssue;
    private Date dateOfIssue;
}
