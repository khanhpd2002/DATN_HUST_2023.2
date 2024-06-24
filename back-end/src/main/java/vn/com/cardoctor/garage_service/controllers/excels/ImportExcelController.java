package vn.com.cardoctor.garage_service.controllers.excels;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.ApiException;
import vn.com.cardoctor.garage_service.services.excels.ImportExcelService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/import-excels")
@Log4j2
public class ImportExcelController {
    @Autowired
    ImportExcelService importExcelService;

    @PostMapping("/{garage-id}/distributors")
    public ResponseEntity<Object> importDistributor(@RequestPart(name = "file") MultipartFile file,
                                                    @PathVariable(name = "garage-id") Long garageId) throws ApiException {
        log.info("Starting import for garage {}", garageId);
        return this.importExcelService.importDistributor(garageId, file);
    }

    @PostMapping("/{inventory-id}/products")
    public ResponseEntity<Object> importProduct(@RequestPart(name = "file") MultipartFile file,
                                                @PathVariable(name = "inventory-id") Long inventoryId) throws ApiException {
        return this.importExcelService.importProduct(inventoryId, file);
    }

    @PostMapping("/{garage-id}/car-customer")
    public ResponseEntity<Object> importCarCustomer(@RequestPart(name = "file") MultipartFile file,
                                                    @PathVariable(name = "garage-id") Long garageId) throws ApiException {
        return this.importExcelService.importCarCustomer(garageId, file);
    }
}
