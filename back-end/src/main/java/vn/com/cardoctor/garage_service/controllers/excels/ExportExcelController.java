package vn.com.cardoctor.garage_service.controllers.excels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.cardoctor.garage_service.services.excels.ExportExcelService;

import java.io.FileNotFoundException;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/export-excels")
public class ExportExcelController {
    @Autowired
    ExportExcelService exportExcelService;

    @PostMapping("/{inventory-id}/products")
    public ResponseEntity<Object> exportProduct(@PathVariable(name = "inventory-id") Long inventoryId) throws FileNotFoundException {
        return this.exportExcelService.exportProduct(inventoryId);
    }
}
