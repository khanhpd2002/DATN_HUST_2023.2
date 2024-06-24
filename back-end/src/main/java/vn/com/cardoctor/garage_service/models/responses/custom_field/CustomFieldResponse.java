package vn.com.cardoctor.garage_service.models.responses.custom_field;

import lombok.Data;

@Data
public class CustomFieldResponse {
    private String fieldType;
    private String fieldName;
    private String label;
    private String values;
}
