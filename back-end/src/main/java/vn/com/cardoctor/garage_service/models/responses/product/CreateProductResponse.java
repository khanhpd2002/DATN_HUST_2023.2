package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;

@Data
public class CreateProductResponse {
    private Long parentProductId;
    private Long productId;
}
