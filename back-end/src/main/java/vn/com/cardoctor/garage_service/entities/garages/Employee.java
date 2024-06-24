package vn.com.cardoctor.garage_service.entities.garages;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    private Integer gender;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private String type;
    private Integer status;
    private Integer contractType;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private long garageId;
    private String phoneNumber;
}
