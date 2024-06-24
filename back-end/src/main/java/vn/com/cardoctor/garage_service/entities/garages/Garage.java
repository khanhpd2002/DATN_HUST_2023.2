package vn.com.cardoctor.garage_service.entities.garages;


import lombok.Data;
import vn.com.cardoctor.garage_service.entities.BaseEntity;
import vn.com.cardoctor.garage_service.models.dtos.GarageInfoDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@SqlResultSetMapping(
        name = "GarageInfoDto",
        classes = {
                @ConstructorResult(
                        targetClass = GarageInfoDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "contact_point_phone", type = String.class),
                                @ColumnResult(name = "contact_point_name", type = String.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "address", type = String.class),
                                @ColumnResult(name = "contact_point_email", type = String.class),
                                @ColumnResult(name = "tax_code", type = String.class),
                                @ColumnResult(name = "logo", type = String.class),
                                @ColumnResult(name = "additional_information", type = String.class),
                        })})


@Entity
@Data
@Table(name = "garages")
public class Garage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String code;
    private String contactPointName;
    private String contactPointPhone;
    private String contactPointEmail;
    private String avatar;
    private String images;
    @Temporal(TemporalType.DATE)
    private Date contractFromDate;
    @Temporal(TemporalType.DATE)
    private Date contractToDate;
    private Integer contractStatus;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private Integer maxCapacity;
    private Integer carLift;
    private Integer maxTonnage;
    private Integer numberGatheringPoint;
    private Long garageGroupId;
    private String open;
    private String close;
    private Integer supportSos;
    private Integer serviceTime;
    private Integer garageType;
    private String description;
    private BigDecimal averageRating;
    private Integer numberOfBooked;
    private Integer numberOfRating;
    private Integer numberOfServiceTicket;
    private Integer garageArea;
    private Integer numEmployee;
    private String equipment;
    private Integer isVerified;
    private Integer status;
    private String logo;
    private String additionalInformation;
}

