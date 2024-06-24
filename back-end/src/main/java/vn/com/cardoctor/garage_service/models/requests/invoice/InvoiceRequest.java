package vn.com.cardoctor.garage_service.models.requests.invoice;

import lombok.Data;
import vn.com.cardoctor.garage_service.models.requests.Image;

import java.util.List;

@Data
public class InvoiceRequest {
    private Integer paymentType;
    private List<Image> images;
}
