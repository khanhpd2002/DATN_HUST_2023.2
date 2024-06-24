package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageInfoDto {
    private Long id;
    private String contractPointPhone;
    private String contactPointName;
    private String name;
    private String address;
    private String contactPointEmail;
    private String taxCode;
    private String logo;
    private String additionalInformation;
}
