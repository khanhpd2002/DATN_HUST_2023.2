package vn.com.cardoctor.garage_service.models.requests.garage;

import lombok.Data;

@Data
public class UpdateLogoRequest {
    private String oldLogo;
    private String logo;
}
