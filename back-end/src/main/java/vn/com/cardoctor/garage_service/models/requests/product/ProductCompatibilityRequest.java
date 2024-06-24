package vn.com.cardoctor.garage_service.models.requests.product;

import lombok.Data;

@Data
public class ProductCompatibilityRequest {
    private Long carBrandId;
    private Long carModelId;
    private Long carYearId;
    private Long carVersionId;
}
