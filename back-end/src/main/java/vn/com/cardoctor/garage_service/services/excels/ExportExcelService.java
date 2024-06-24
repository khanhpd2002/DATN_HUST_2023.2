package vn.com.cardoctor.garage_service.services.excels;

import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.entities.Category;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.repositories.CategoryRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ExportExcelService extends BaseExcelService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private static final String EXPORT_TEMPLATE = "Template";

    public ResponseEntity<Object> exportProduct(Long inventoryId) throws FileNotFoundException {
        PagingDataResponse response = this.productRepository.findAllParentProduct(inventoryId, null, null, null, null, Integer.MAX_VALUE, 1);
        List<Product> products = (List<Product>) response.getData();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("templates/export-data/Template_export_Tonkho.xlsx"))) {
            XSSFCellStyle style = workbook.createCellStyle();
            style.setBorderTop(BorderStyle.MEDIUM);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            XSSFSheet sheet = workbook.getSheet(ExportExcelService.EXPORT_TEMPLATE);
            int firstRow = 1;
            for (int i = 0; i < products.size(); i++) {
                XSSFRow row;
                row = sheet.getRow(firstRow + i);
                log.info("row index is {}", firstRow + i);

                this.setCellProductData(products.get(i), firstRow, i, sheet, row);
            }
            FileOutputStream os = new FileOutputStream("templates/export-data/Tonkho.xlsx");
            workbook.write(os);
            InputStreamResource newFile = new InputStreamResource(new FileInputStream("templates/export-data/Tonkho.xlsx"));
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Tonkho.xlsx").contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(newFile);
        } catch(Exception e) {
            log.info("error export is {}", e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void setCellProductData(Product product, Integer firstRow, Integer index, XSSFSheet sheet, XSSFRow row) {
        try {
            int currentCol = 0;
            if (row == null) {
                row = sheet.createRow(firstRow + index);
                row.createCell(currentCol);
                XSSFCell indexCell = row.getCell(currentCol++);
                indexCell.setCellValue(index + 1);

                row.createCell(currentCol);
                XSSFCell codeCell = row.getCell(currentCol++);
                codeCell.setCellValue(product.getCode());

                row.createCell(currentCol);
                XSSFCell nameCell = row.getCell(currentCol++);
                nameCell.setCellValue(product.getName());

                row.createCell(currentCol);
                XSSFCell unitCell = row.getCell(currentCol++);
                unitCell.setCellValue(product.getUnit());

                row.createCell(currentCol);
                XSSFCell sparePartTypeCell = row.getCell(currentCol++);
                Optional<Category> oCategory = product.getSparePartType() != null ? this.categoryRepository.findById(Long.valueOf(product.getSparePartType())) : Optional.empty();
                if (oCategory.isPresent()) {
                    sparePartTypeCell.setCellValue(oCategory.get().getName());
                } else {
                    sparePartTypeCell.setCellValue("Không rõ");
                }

                row.createCell(currentCol);
                XSSFCell quantityCell = row.getCell(currentCol);
                quantityCell.setCellValue(product.getQuantity().doubleValue());
            } else {
                XSSFCell indexCell = row.getCell(currentCol++);
                indexCell.setCellValue(index);
                XSSFCell codeCell = row.getCell(currentCol++);
                codeCell.setCellValue(product.getCode());
                XSSFCell nameCell = row.getCell(currentCol++);
                nameCell.setCellValue(product.getName());
                XSSFCell unitCell = row.getCell(currentCol++);
                unitCell.setCellValue(product.getUnit());
                XSSFCell sparePartTypeCell = row.getCell(currentCol++);
                Optional<Category> oCategory = this.categoryRepository.findById(Long.valueOf(product.getSparePartType()));
                if (oCategory.isPresent()) {
                    sparePartTypeCell.setCellValue(oCategory.get().getName());
                } else {
                    sparePartTypeCell.setCellValue("Không rõ");
                }
                XSSFCell quantityCell = row.getCell(currentCol);
                quantityCell.setCellValue(product.getQuantity().doubleValue());
            }
        } catch (Exception e) {
            log.info("error set cell value is {}", e.getMessage());
        }
    }
}
