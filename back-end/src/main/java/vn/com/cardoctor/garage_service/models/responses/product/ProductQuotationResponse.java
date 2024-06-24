package vn.com.cardoctor.garage_service.models.responses.product;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.requests.Image;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductQuotationResponse {
    private Long id;
    // Loai bao gia bang anh hoac dich vu
    private Integer type;
    // Type la phu tung hay dich vu
    private Integer productType;
    private Long productId;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal discount;
    private List<Image> images;
}
