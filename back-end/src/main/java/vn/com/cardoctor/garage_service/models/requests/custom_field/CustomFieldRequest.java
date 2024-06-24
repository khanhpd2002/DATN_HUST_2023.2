package vn.com.cardoctor.garage_service.models.requests.custom_field;

import lombok.Data;

import java.util.List;

@Data
public class CustomFieldRequest {
    private Long id;
    private String resourceName;
    private Long modelId;
    private String fieldType;
    private String fieldName;
    private String label;
    private Integer visible;
    private Integer isRequired;
    private List<CustomFieldOptionRequest> customFieldOptionRequests;
}
