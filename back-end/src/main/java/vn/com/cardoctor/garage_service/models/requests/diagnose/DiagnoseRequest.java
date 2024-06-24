package vn.com.cardoctor.garage_service.models.requests.diagnose;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.models.requests.Image;
import vn.com.cardoctor.garage_service.models.requests.product.ProductOrder;

import java.util.List;

@Data
public class DiagnoseRequest {
    Long employeeId;
    String description;
    List<String> images;
}
