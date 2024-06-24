package vn.com.cardoctor.garage_service.models.requests.custom_field;

import lombok.Data;

@Data
public class CustomFieldValueRequest {
    private Long customFieldId;
    private Long optionValue;
    private String textValue;
}
