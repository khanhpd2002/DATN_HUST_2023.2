package vn.com.cardoctor.garage_service.models.responses.employee;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeResponse {
    private long id;
    private long garageId;
    private String fullName;
    private Integer gender;
    private Date birthday;
    private String type;
    private Integer status;
    private Integer contractType;
    private Date startDate;
    private Date endDate;
    private String phoneNumber;
}
