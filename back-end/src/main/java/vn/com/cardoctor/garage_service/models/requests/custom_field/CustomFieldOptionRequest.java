package vn.com.cardoctor.garage_service.models.requests.custom_field;

import lombok.Data;

@Data
public class CustomFieldOptionRequest {
    private Long customFieldId;
    private String name;
}
