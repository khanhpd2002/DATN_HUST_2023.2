package vn.com.cardoctor.garage_service.entities.rest;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExpertRestEntity {

    private Long id;

    private String name;

    private String username;

    private String phone;

    private String email;

    private String gender;

    private String birthday;

    private String avatar;

    private String identifiedCard;

    private String educationLevel;

    private Integer expYears;

    private String contractType;

    private String workplace;

    private String address;

    private Long wardId;

    private Long districtId;

    private Long provinceId;

    private Long bankId;

    private String accountNumber;

    private String acceptDate;

    private String acceptUser;

    private String status;

    private String expertProfileFiles;

    private String strStatus;

    private String strGender;

    private String strBirthday;

}

