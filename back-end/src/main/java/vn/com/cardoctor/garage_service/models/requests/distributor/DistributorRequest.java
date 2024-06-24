package vn.com.cardoctor.garage_service.models.requests.distributor;

import lombok.Data;

@Data
public class DistributorRequest {
    private String code;
    private String name;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private String address;
    private String contactName;
    private String contactPhone;
}
