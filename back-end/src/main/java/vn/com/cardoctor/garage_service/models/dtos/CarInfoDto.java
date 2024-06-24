package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInfoDto {
    private Long carBrandId;
    private String carBrand;
    private Long carModelId;
    private String carModel;
    private Long carYearId;
    private String carYear;
    private Long carVersionId;
    private String carVersion;
    private String carBrandUnique;
    private String carBrandModel;
    private String carModelUnique;
    private String carModelYear;
    private String carYearUnique;
    private String carYearVersion;
    private String carVersionUnique;
}
