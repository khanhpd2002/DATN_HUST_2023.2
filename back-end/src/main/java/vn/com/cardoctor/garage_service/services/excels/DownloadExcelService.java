package vn.com.cardoctor.garage_service.services.excels;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.CustomerType;
import vn.com.cardoctor.garage_service.models.dtos.CarInfoDto;
import vn.com.cardoctor.garage_service.repositories.CustomerTypeRepository;
import vn.com.cardoctor.garage_service.repositories.external.CarInfoRepositoryImpl;
import vn.com.cardoctor.garage_service.services.FileStorageService;
import vn.com.cardoctor.garage_service.utils.excel.Jxls2xTransformerUtils;
import vn.com.cardoctor.garage_service.utils.excel.commands.DataValidationDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class DownloadExcelService {
    private final CarInfoRepositoryImpl carInfoRepository;

    private final CustomerTypeRepository customerTypeRepository;

    private final FileStorageService fileStorageService;

    private static final String S_S_XLSX = "%s_%s.xlsx";

    private static final String PREPARE_DATA = "prepare-data";

    private static final String DATA = "data";

    private static final String IMPORT_TEMPLATE = "import-template";

    private static final String MAP_DETAIL = "mapDetail";

    private static final String SHEET_NAMES = "sheetNames";

    private static final String SHEETNAMESS = "sheetNamess";

    private static final String LENGTH_BRAND = "lengthBrand";

    private static final String LENGTH_MODEL = "lengthModel";

    private static final String LENGTH_YEAR = "lengthYear";

    private static final String LENGTH_VERSION = "lengthVersion";

    public ResponseEntity<Object> downloadTemplateDistributor() throws IOException {
        InputStreamResource file = new InputStreamResource(new FileInputStream("templates/import-data/Template_import_NPP.xlsx"));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Template_import_NPP.xlsx").contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    public ResponseEntity<Object> downloadTemplateProduct() throws IOException {
        InputStreamResource file = new InputStreamResource(new FileInputStream("templates/import-data/Template_import_Phutung.xlsx"));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Template_import_Phutung.xlsx").contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    public ResponseEntity<Object> downloadTemplateCarCustomerStatic() throws IOException {
        InputStreamResource file = new InputStreamResource(new FileInputStream("templates/import-data/Template_import_CarCustomer.xlsx"));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "Template_import_CarCustomer.xlsx").contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    public ResponseEntity<Object> downloadTemplateCarCustomer() {
        try {
            Map<String, List<String>> mapDetail = new HashMap<>();
            int firstRow = 4;
            List<CarInfoDto> dataCars = this.carInfoRepository.findAllCarInfo();
            for (CarInfoDto carInfoDto : dataCars) {
                carInfoDto.setCarBrand(carInfoDto.getCarBrand() + "-" + carInfoDto.getCarBrandId().toString());
                carInfoDto.setCarModel(carInfoDto.getCarModel() + "-" + carInfoDto.getCarModelId().toString());
                carInfoDto.setCarYear(carInfoDto.getCarYear() + "-" + carInfoDto.getCarYearId().toString());
                carInfoDto.setCarVersion(carInfoDto.getCarVersion() + "-" + carInfoDto.getCarVersionId().toString());
            }
            Map<String, Object> result = this.sortCarInfoDto(dataCars);
            List<CustomerType> customerTypes = this.customerTypeRepository.findByGarageIdIsNull();
            List<String> customerTypeName = customerTypes.stream()
                    .map(customerType -> customerType.getCustomerTypeName() + "-" + customerType.getId())
                    .collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            mapDetail.put("customerType", customerTypeName);
            map.put(DownloadExcelService.MAP_DETAIL, mapDetail);
            map.put(DownloadExcelService.SHEET_NAMES, new ArrayList<>(mapDetail.keySet()));
            map.put(DownloadExcelService.SHEETNAMESS, new ArrayList<>(mapDetail.keySet()));
            map.put("dataCars", result.get(DownloadExcelService.DATA));

            // EXPORT TEMPLATE
            String outputFilePath = Jxls2xTransformerUtils.poiTransformer("templates/import-data/Template_import_CarCustomer_resource.xlsx",
                    map, this.fileStorageService.getTempExportExcel(),
                    "Template_import_CarCustomer.xlsx",
                    ImmutableMap.<String, String>builder()
                            .put(DownloadExcelService.PREPARE_DATA, DownloadExcelService.IMPORT_TEMPLATE)
                            .put("prepare-data-car", DownloadExcelService.DATA).build(),
                    ImmutableMap.<String, String>builder()
                            .put(DownloadExcelService.DATA, DownloadExcelService.DATA).build());

            File file = new File(outputFilePath);
            if (!file.exists()) {
                throw new ApiException(ERROR.BAD_REQUEST, "File not exists");
            }
            int lengthBrand = (int) result.get(DownloadExcelService.LENGTH_BRAND);
            int lengthModel = (int) result.get(DownloadExcelService.LENGTH_MODEL);
            int lengthYear = (int) result.get(DownloadExcelService.LENGTH_YEAR);
            int lengthVersion = (int) result.get(DownloadExcelService.LENGTH_VERSION);
            DataValidationDto customerTypeDataValidationDto = new DataValidationDto(5, 5, 3, 3,
                    "customerType!$A:$A");
            DataValidationDto carBrandDataValidationDto = new DataValidationDto(5, 5, 4, 4,
                    "data!$G$5:$G$" + (firstRow + lengthBrand));
            DataValidationDto carModelDataValidationDto = new DataValidationDto(5, 5, 5, 5,
                    "OFFSET(data!$I$5, MATCH(E6,data!$I$5:$I$" + (firstRow + lengthModel)
                            + ",0)-1, 1,COUNTIFS(data!$I$5:$I$" + (firstRow + lengthModel) + ",E6))");
            DataValidationDto carYearDataValidationDto = new DataValidationDto(5, 5, 6, 6,
                    "OFFSET(data!$L$5, MATCH(F6,data!$L$5:$L$" + (firstRow + lengthYear)
                            + ",0)-1, 1,COUNTIFS(data!$L$5:$L$" + (firstRow + lengthYear) + ",F6))");
            DataValidationDto carVersionDataValidationDto = new DataValidationDto(5, 5, 7, 7,
                    "OFFSET(data!$O$5, MATCH(G6,data!$O$5:$O$" + (firstRow + lengthVersion)
                            + ",0)-1, 1,COUNTIFS(data!$O$5:$O$" + (firstRow + lengthVersion) + ",G6))");
            Jxls2xTransformerUtils.addDataValidation(outputFilePath, DownloadExcelService.IMPORT_TEMPLATE,
                    Arrays.asList(customerTypeDataValidationDto, carBrandDataValidationDto, carModelDataValidationDto,
                            carYearDataValidationDto, carVersionDataValidationDto));
//            Resource resource = this.fileStorageService.loadFileAsResource(outputFilePath);
//            HttpHeaders headers = this.fileStorageService.loadHttpHeaders(resource);
//            Resource resource = new Resource;
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    Map<String, Object> sortCarInfoDto(List<CarInfoDto> dataCars) {
        Map<String, Object> result = new HashMap<>();
        Map<String, CarInfoDto> mapBrand = new HashMap<>();
        Map<String, CarInfoDto> mapModel = new HashMap<>();
        Map<String, CarInfoDto> mapYear = new HashMap<>();
        Map<String, CarInfoDto> mapVersion = new HashMap<>();

        List<CarInfoDto> dataBrand = new ArrayList<>();
        List<CarInfoDto> dataModel = new ArrayList<>();
        List<CarInfoDto> dataYear = new ArrayList<>();
        List<CarInfoDto> dataVersion = new ArrayList<>();
        for (CarInfoDto a : dataCars) {
            String keyBrand = a.getCarBrand();
            String keyModel = a.getCarBrand() + "-" + a.getCarModel();
            String keyYear = a.getCarBrand() + "-" + a.getCarModel() + "-" + a.getCarYear();
            String keyVersion = a.getCarBrand() + "-" + a.getCarModel() + "-" + a.getCarYear() + "-" + a.getCarVersion();
            if (mapBrand.get(keyBrand) == null) {
                mapBrand.put(keyBrand, a);
                dataBrand.add(a);
            }
            if (mapModel.get(keyModel) == null) {
                mapModel.put(keyModel, a);
                dataModel.add(a);
            }
            if (mapYear.get(keyYear) == null) {
                mapYear.put(keyYear, a);
                dataYear.add(a);
            }
            if (mapVersion.get(keyVersion) == null) {
                mapVersion.put(keyVersion, a);
                dataVersion.add(a);
            }
        }


        int lengthBrand = dataBrand.size();
        int lengthModel = dataModel.size();
        int lengthYear = dataYear.size();
        int lengthVersion = dataVersion.size();

        for (int i = 0; i < lengthVersion; i++) {
            dataCars.get(i).setCarYearVersion(dataVersion.get(i).getCarYear());
            dataCars.get(i).setCarVersionUnique(dataVersion.get(i).getCarVersion());
        }

        for (int i = 0; i < lengthYear; i++) {
            dataCars.get(i).setCarModelYear(dataYear.get(i).getCarModel());
            dataCars.get(i).setCarYearUnique(dataYear.get(i).getCarYear());
        }

        for (int i = 0; i < lengthModel; i++) {
            dataCars.get(i).setCarBrandModel(dataModel.get(i).getCarBrand());
            dataCars.get(i).setCarModelUnique(dataModel.get(i).getCarModel());
        }

        for (int i = 0; i < lengthBrand; i++) {
            dataCars.get(i).setCarBrandUnique(dataBrand.get(i).getCarBrand());
        }

        result.put(DownloadExcelService.DATA, dataCars);
        result.put(DownloadExcelService.LENGTH_BRAND, lengthBrand);
        result.put(DownloadExcelService.LENGTH_MODEL, lengthModel);
        result.put(DownloadExcelService.LENGTH_YEAR, lengthYear);
        result.put(DownloadExcelService.LENGTH_VERSION, lengthVersion);
        return result;

    }
}

