package vn.com.cardoctor.garage_service.models.responses.distributor;

import lombok.Data;

@Data
public class DistributorResponse {
    private long id;
    private String code;
    private String name;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address;
    private String contactName;
    private String contactPhone;
    private Long garageId;
}
