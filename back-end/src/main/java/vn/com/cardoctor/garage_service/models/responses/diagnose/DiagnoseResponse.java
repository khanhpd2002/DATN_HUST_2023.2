package vn.com.cardoctor.garage_service.models.responses.diagnose;

import vn.com.cardoctor.garage_service.models.responses.employee.EmployeeResponse;
import lombok.Data;
import vn.com.cardoctor.garage_service.models.responses.product.ProductQuotationResponse;
import vn.com.cardoctor.garage_service.models.responses.product.ProductResponse;

import java.util.List;

@Data
public class DiagnoseResponse {
    private int status;
    private EmployeeResponse employeeResponse;
    private String description;
    private List<ProductQuotationResponse> productResponses;
}
