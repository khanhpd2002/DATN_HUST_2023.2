package vn.com.cardoctor.garage_service.entities.garages;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "garage_owners")
@Data
public class GarageOwner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;
    private String userName;
    private String phone;
    private String email;
    private String identifiedCard;
    @Temporal(TemporalType.DATE)
    private Date dateOfIssue;
    private String placeOfIssue;
    private Integer gender;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    private String avatar;
    private String keycloakId;
    private Integer status;
}
