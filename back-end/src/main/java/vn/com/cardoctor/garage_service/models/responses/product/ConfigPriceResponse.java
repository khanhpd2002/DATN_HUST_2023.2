package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;

import java.util.List;

@Data
public class ConfigPriceResponse {
    private Integer type;
    private Long productId;
    private String productName;
    private String productCode;
    private Long garageServiceId;
    private String garageServiceName;
    private String garageServiceCode;
    private List<ConfigPriceDto> configPrices;
    private Long garageId;
}
