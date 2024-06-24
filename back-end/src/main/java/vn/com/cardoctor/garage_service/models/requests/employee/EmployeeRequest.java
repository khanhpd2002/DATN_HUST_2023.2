package vn.com.cardoctor.garage_service.models.requests.employee;

import lombok.Data;

import java.util.Date;

@Data
public class EmployeeRequest {
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
