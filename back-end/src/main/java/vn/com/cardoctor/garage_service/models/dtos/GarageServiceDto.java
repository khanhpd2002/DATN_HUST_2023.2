package vn.com.cardoctor.garage_service.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageServiceDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String garageServiceType;
    private Long garageId;
}
