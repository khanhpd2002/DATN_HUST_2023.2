package vn.com.cardoctor.garage_service.models.responses.dashboards;

import lombok.Data;

@Data
public class CountProductResponse {
    private Long totalProductCode;
    private Long totalProductQuantity;
}
