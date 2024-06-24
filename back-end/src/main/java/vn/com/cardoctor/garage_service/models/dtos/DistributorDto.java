package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorDto {
    private long id;
    private String code;
    private String name;
    private Long provinceId;
    private String provinceName;
    private Long districtId;
    private String districtName;
    private Long wardId;
    private String wardName;
    private String address;
    private String contactName;
    private String contactPhone;
    private Long garageId;
    private String garageName;
}
