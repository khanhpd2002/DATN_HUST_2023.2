package vn.com.cardoctor.garage_service.models.requests.tag;

import lombok.Data;

@Data
public class TagRequest {
    private String name;
    private String description;
}
