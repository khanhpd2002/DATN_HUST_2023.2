package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class WorkOrderDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String subject;
    private String description;
    private Integer guaranteeType;
    private Long modelId;
    private Long brandId;
    private Long fuelId;
    private String licensePlate;
    private Long chassisNumber;
    private String fullName;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address;
    private String email;
    private String phoneNumber;
    private Date scheduledDate;
    private Date actualEndDate;
}
