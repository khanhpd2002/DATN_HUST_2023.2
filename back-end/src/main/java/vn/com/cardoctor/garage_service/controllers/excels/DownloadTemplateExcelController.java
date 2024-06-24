package vn.com.cardoctor.garage_service.controllers.excels;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ApiException;
import vn.com.cardoctor.garage_service.services.excels.DownloadExcelService;

import java.io.IOException;

@RestController
@RequestMapping("/download-template")
public class DownloadTemplateExcelController {
    private final DownloadExcelService downloadExcelService;

    public DownloadTemplateExcelController(DownloadExcelService downloadExcelService) {
        this.downloadExcelService = downloadExcelService;
    }

    @GetMapping("/distributor")
    public ResponseEntity<Object> downloadTemplateExcelDistributor() throws IOException {
        return this.downloadExcelService.downloadTemplateDistributor();
    }

    @GetMapping("/product")
    public ResponseEntity<Object> downloadTemplateExcelProduct() throws IOException {
        return this.downloadExcelService.downloadTemplateProduct();
    }

    @GetMapping("/car-customer")
    public ResponseEntity<Object> downloadTemplateExcelCarCustomer() throws IOException, ApiException {
        return this.downloadExcelService.downloadTemplateCarCustomerStatic();
    }
}
