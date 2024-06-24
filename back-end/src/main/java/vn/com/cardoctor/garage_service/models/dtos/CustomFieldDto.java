package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CustomFieldDto {
    @Id
    private long id;
    private String fieldType;
    private String fieldName;
    private String label;
    private String textValue;
    private Long optionValue;
}
