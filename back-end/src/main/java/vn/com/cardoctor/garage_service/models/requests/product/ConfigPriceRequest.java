package vn.com.cardoctor.garage_service.models.requests.product;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.responses.product.ConfigPriceCustomerType;

import java.util.List;

@Data
public class ConfigPriceRequest {
    private Long modelId;
    private Integer type;
    private List<ConfigPriceCustomerType> configPrices;
}
