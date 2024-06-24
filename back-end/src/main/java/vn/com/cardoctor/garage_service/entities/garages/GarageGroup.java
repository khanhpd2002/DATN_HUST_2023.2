package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "garage_groups")
public class GarageGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private Integer status;
}
