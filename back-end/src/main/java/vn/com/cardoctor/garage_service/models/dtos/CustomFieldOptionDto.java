package vn.com.cardoctor.garage_service.models.dtos;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CustomFieldOptionDto {
    @Id
    private long id;
    private Long customFieldId;
    private String values;
    private String fieldType;
    private String fieldName;
    private String label;
}
