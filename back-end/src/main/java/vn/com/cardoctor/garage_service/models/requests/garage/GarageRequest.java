package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;
import vn.com.cardoctor.garage_service.entities.garages.Insurance;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;
import vn.com.cardoctor.garage_service.models.requests.custom_field.CustomFieldValueRequest;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GarageRequest {
    private Long id;
    private String name;
    private String contactPointName;
    private String contactPointPhone;
    private String contactPointEmail;
    private String avatar;
    private List<String> images;
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
//    private BigDecimal averageRating;
//    private Integer numberOfBooked;
//    private Integer numberOfRating;
//    private Integer numberOfServiceTicket;
    private Integer garageArea;
    private Integer numEmployee;
    private Integer equipment;
    private Integer isVerified;
    private List<CarSubSystem> carSubSystems;
    private List<Rescue> rescues;
    private List<Insurance> insurances;
    private List<CustomFieldValueRequest> customFieldValueRequests;
}
