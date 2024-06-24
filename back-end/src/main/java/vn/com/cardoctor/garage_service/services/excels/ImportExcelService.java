package vn.com.cardoctor.garage_service.services.excels;

import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.rest.Area;
import vn.com.cardoctor.garage_service.models.requests.car.CarCustomerRequest;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.repositories.*;
import vn.com.cardoctor.garage_service.services.FileStorageService;
import vn.com.cardoctor.garage_service.services.ProductService;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@SuppressWarnings("unchecked")
public class ImportExcelService extends BaseExcelService {
    @Autowired
    AreaRepository areaRepository;

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CarRepository carRepository;

    private static final String IMPORT_TEMPLATE = "Template";

    private static final String _IMPORT_TEMPLATE = "import-template";

    public ResponseEntity<Object> importDistributor(Long garageId, MultipartFile file) throws ApiException {
        List<Distributor> results = new ArrayList<>();
        OutputStream outputStream = null;
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheet(ImportExcelService.IMPORT_TEMPLATE);
            int firstRow = 1;
            int lastCol = 8;
            boolean isError = false;
            String errorMsg = "";

            for (int i = firstRow; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (containsValue(row, row.getFirstCellNum(), row.getLastCellNum())) {
                    Distributor distributor = this.convertRow2Distributor(garageId, row);

                    if (StringUtil.isNullOrEmpty(distributor.getErrors())) {
                        results.add(distributor);
                    } else {
                        isError = true;
                        errorMsg = distributor.getErrors();
                        XSSFCell cell = sheet.getRow(i).createCell(lastCol);
                        cell.setCellValue(distributor.getErrors());
                    }
                }
            }

            if (isError) {
                String relativePath = this.fileStorageService.saveFile("temp", file);
                String tempPath = this.fileStorageService.getAbsolutePath(relativePath);
                outputStream = new FileOutputStream(tempPath);
                workbook.write(outputStream);
                throw new ApiException(ERROR.INVALID_REQUEST, errorMsg);
            }
            return new ResponseEntity<>(this.distributorRepository.saveAll(results), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Đi đến đây rồi nhé!");
            log.error(e.getMessage(), e);
            throw new ApiException(ERROR.BAD_REQUEST, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private Distributor convertRow2Distributor(Long garageId, XSSFRow row) {
        Distributor distributor = new Distributor();
        try {
            int lastCol = 0;
            log.info("code is {}", this.getStringFromRow(row, lastCol));
            String code = this.getStringFromRow(row, lastCol++);
            if (StringUtil.isNullOrEmpty(code)) {
                throw new ApiException(ERROR.BAD_REQUEST, "Tải file không thành công do tồn tại dòng chứa mã NPP trống");
            }
            distributor.setCode(code);
            Optional<Distributor> oDistributor = this.distributorRepository.findByCodeAndGarageId(distributor.getCode(), garageId);
            if (oDistributor.isPresent()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Tồn tại hàng với mã NPP đã tồn tại");
            }
            String name = this.getStringFromRow(row, lastCol++);
            if (StringUtil.isNullOrEmpty(name)) {
                throw new ApiException(ERROR.BAD_REQUEST, "Tải file không thành công do tồn tại dòng chứa tên NPP trống");
            }
            distributor.setName(name);
            String province = this.getStringFromRow(row, lastCol++);
            if (!StringUtils.isEmpty(province)) {
                Long provinceId = this.areaRepository.findAreaByNameAndParentId(province, null);
                distributor.setProvinceId(provinceId);
                String district = this.getStringFromRow(row, lastCol++);
                if (!StringUtils.isEmpty(district)) {
                    Long districtId = this.areaRepository.findAreaByNameAndParentId(district, provinceId);
                    distributor.setDistrictId(districtId);
                    String ward = this.getStringFromRow(row, lastCol++);
                    if (!StringUtils.isEmpty(ward)) {
                        Long wardId = this.areaRepository.findAreaByNameAndParentId(ward, districtId);
                        distributor.setWardId(wardId);
                    }
                } else {
                    lastCol += 1;
                }
            } else {
                lastCol += 2;
            }
            distributor.setAddress(this.getStringFromRow(row, lastCol++));
            distributor.setContactName(this.getStringFromRow(row, lastCol++));
            distributor.setContactPhone(this.getStringFromRow(row, lastCol));
            distributor.setGarageId(garageId);
            distributor.setIsDelete(0);
        } catch (Exception e) {
            distributor.setErrors(e.getMessage());
            log.info("error is {}", e.getMessage());
        }
        return distributor;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> importProduct(Long inventoryId, MultipartFile file) throws ApiException {
        OutputStream outputStream = null;
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheet(ImportExcelService.IMPORT_TEMPLATE);
            int firstRow = 1;
            int lastCol = 6;
            boolean isError = false;
            String errorMsg = "";

            for (int i = firstRow; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (containsValue(row, row.getFirstCellNum(), row.getLastCellNum())) {
                    ProductRequest request = this.convertRow2Product(inventoryId, row);

                    if (StringUtil.isNullOrEmpty(request.getErrors())) {
                        this.productService.create(request, inventoryId);
                    } else {
                        isError = true;
                        errorMsg = request.getErrors();
                        XSSFCell cell = sheet.getRow(i).createCell(lastCol);
                        cell.setCellValue(request.getErrors());
                    }
                } else {
                    break;
                }
            }

            if (isError) {
                String relativePath = this.fileStorageService.saveFile("temp", file);
                String tempPath = this.fileStorageService.getAbsolutePath(relativePath);
                outputStream = new FileOutputStream(tempPath);
                workbook.write(outputStream);
                throw new ApiException(ERROR.INVALID_REQUEST, errorMsg);
            }
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(ERROR.BAD_REQUEST, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private ProductRequest convertRow2Product(Long inventoryId, XSSFRow row) {
        ProductRequest request = new ProductRequest();
        try {
            int lastCol = 0;
            log.info("code npp is {}", this.getStringFromRow(row, lastCol));
            String distributorCode = this.getStringFromRow(row, lastCol++);
            Optional<Inventory> oInventory = this.inventoryRepository.findById(inventoryId);
            Optional<Distributor> oDistributor = this.distributorRepository.findByCodeAndGarageId(distributorCode, oInventory.orElseThrow().getGarageId());
            if (oDistributor.isEmpty()) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Mã NPP không tồn tại");
            }
            request.setDistributorId(oDistributor.get().getId());
            log.info("Mã phụ tùng is {}", this.getStringFromRow(row, lastCol));
            String productCode = this.getStringFromRow(row, lastCol++);
            Optional<Product> oProduct = this.productRepository.findByCodeAndDistributorId(productCode, oDistributor.get().getId());
            if (oProduct.isPresent()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Mã phụ tùng đã tồn tại ứng với NPP");
            }
            request.setCode(productCode);
            log.info("Tên phụ tùng is {}", this.getStringFromRow(row, lastCol));
            String name = this.getStringFromRow(row, lastCol++);
            request.setName(name);
            log.info("ĐVT phụ tùng is {}", this.getStringFromRow(row, lastCol));
            String unit = this.getStringFromRow(row, lastCol++);
            request.setUnit(unit);
            log.info("Giá nhập phụ tùng is {}", this.getStringFromRow(row, lastCol));
            BigDecimal purchasePrice = new BigDecimal(this.getStringFromRow(row, lastCol++));
            request.setPurchasePrice(purchasePrice);
            log.info("Loại phụ tùng is {}", this.getStringFromRow(row, lastCol));
            String type = this.getStringFromRow(row, lastCol);
            if (!StringUtil.isNullOrEmpty(type)) {
                Optional<Category> oCategory = this.categoryRepository.findByName(type);
                if (oCategory.isEmpty()) {
                    throw new ApiException(ERROR.BAD_REQUEST, "Loại phụ tùng không hợp lệ");
                }
                request.setSparePartType(oCategory.get().getId().intValue());
            }
        } catch (Exception e) {
            request.setErrors(e.getMessage());
            log.info("error is {}", e.getMessage());
        }
        return request;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> importCarCustomer(Long garageId, MultipartFile file) throws ApiException {
        OutputStream outputStream = null;
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = workbook.getSheet(ImportExcelService._IMPORT_TEMPLATE);
            int firstRow = 5;
            int lastCol = 11;
            boolean isError = false;
            String errorMsg = "";
            Map<Customer, List<Car>> carCustomers = new HashMap<>();
            List<CarCustomerRequest> carCustomerRequestList = new ArrayList<>();
            for (int i = firstRow; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (containsValue(row, row.getFirstCellNum(), row.getLastCellNum())) {
                    CarCustomerRequest request = this.convertRow2CarCustomer(garageId, row);

                    if (StringUtil.isNullOrEmpty(request.getErrors())) {
                        log.info("request is {}", request.toString());
                        carCustomerRequestList.add(request);
                    } else {
                        isError = true;
                        errorMsg = request.getErrors();
                        XSSFCell cell = sheet.getRow(i).createCell(lastCol);
                        cell.setCellValue(request.getErrors());
                        break;
                    }
                } else {
                    break;
                }
            }

            if (isError) {
                String relativePath = this.fileStorageService.saveFile("temp", file);
                String tempPath = this.fileStorageService.getAbsolutePath(relativePath);
                outputStream = new FileOutputStream(tempPath);
                workbook.write(outputStream);
                throw new ApiException(ERROR.INVALID_REQUEST, errorMsg);
            }

            Set<CarCustomerRequest> carCustomerRequests = new HashSet<>(carCustomerRequestList);
            for (CarCustomerRequest request : carCustomerRequests) {
                Customer customer = new Customer();
                customer.setPhoneNumber(request.getPhone());
                customer.setFullName(request.getName());
                customer.setCustomerTypeId(request.getCustomerTypeId());
                customer.setGarageId(request.getGarageId());

                Car car = new Car();
                car.setCarBrandId(request.getCarBrandId());
                car.setCarModelId(request.getCarModelId());
                car.setCarYearId(request.getCarYearId());
                car.setCarVersionId(request.getCarVersionId());
                car.setCarName(request.getOtherCar());
                car.setLicensePlate(request.getLicensePLate());
                car.setVinNumber(request.getVinNumber());
                car.setGarageId(request.getGarageId());

                carCustomers.computeIfAbsent(customer, k -> new ArrayList<>());
                carCustomers.get(customer).add(car);
            }

            for (Map.Entry<Customer, List<Car>> entry : carCustomers.entrySet()) {
                Customer customer = entry.getKey();
                List<Car> cars = entry.getValue();
                customer = this.customerRepository.save(customer);
                for (Car car : cars) {
                    car.setCustomerId(customer.getId());
                }
                this.carRepository.saveAll(cars);
            }
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(ERROR.BAD_REQUEST, e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private CarCustomerRequest convertRow2CarCustomer(Long garageId, XSSFRow row) {
        CarCustomerRequest request = new CarCustomerRequest();
        request.setGarageId(garageId);
        try {
            int lastCol = 1;
            log.info("phone is {}", this.getStringFromRow(row, lastCol));
            String phone = this.getStringFromRow(row, lastCol++);
            if (phone.isBlank()) {
                throw new ApiException(ERROR.BAD_REQUEST, "SĐT không được để trống");
            }
            if (!phone.matches("\\d{10}")) {
                throw new ApiException(ERROR.BAD_REQUEST, "SĐT không hợp lệ");
            }
            Optional<Customer> oCustomer = this.customerRepository.findFirstByPhoneNumberAndGarageId(phone, garageId);
            if (oCustomer.isPresent()) {
                throw new ApiException(ERROR.BAD_REQUEST, "SĐT đã tồn tại");
            }
            request.setPhone(phone);
            String name = this.getStringFromRow(row, lastCol++);
            log.info("name is {}", name);
            if (name.isBlank()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Tên khách hàng không được để trống");
            }
            request.setName(name);

            String customerType = this.getStringFromRow(row, lastCol++);
            log.info("customerType is {}", customerType);
            if (customerType.isBlank()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Nhóm khách hàng không được để trống");
            }
            try {
                Long customerTypeId = Long.valueOf(customerType.substring(customerType.lastIndexOf("-") + 1));
                request.setCustomerTypeId(customerTypeId);
            } catch (Exception e) {
                throw new ApiException(ERROR.BAD_REQUEST, "Nhóm khách hàng không hợp lệ");
            }

            String carBrand = this.getStringFromRow(row, lastCol++);
            log.info("carBrand is {}", carBrand);
            String carModel = this.getStringFromRow(row, lastCol++);
            log.info("carModel is {}", carModel);
            String carYear = this.getStringFromRow(row, lastCol++);
            log.info("carYear is {}", carYear);
            String carVersion = this.getStringFromRow(row, lastCol++);
            log.info("carVersion is {}", carVersion);
            String otherCar = this.getStringFromRow(row, lastCol++);
            log.info("otherCar is {}", otherCar);

            if (StringUtil.isNullOrEmpty(otherCar)) {
                if (StringUtil.isNullOrEmpty(carBrand) || StringUtil.isNullOrEmpty(carModel) ||
                        StringUtil.isNullOrEmpty(carYear) || StringUtil.isNullOrEmpty(carVersion)) {
                    throw new ApiException(ERROR.BAD_REQUEST, "Không được để trống thông tin cơ bản của xe");
                }
                Long carBrandId = Long.valueOf(carBrand.substring(carBrand.lastIndexOf("-") + 1));
                request.setCarBrandId(carBrandId);

                Long carModelId = Long.valueOf(carModel.substring(carModel.lastIndexOf("-") + 1));
                request.setCarModelId(carModelId);

                Long carYearId = Long.valueOf(carYear.substring(carYear.lastIndexOf("-") + 1));
                request.setCarYearId(carYearId);

                Long carVersionId = Long.valueOf(carVersion.substring(carVersion.lastIndexOf("-") + 1));
                request.setCarVersionId(carVersionId);
            } else {
                if ((!StringUtil.isNullOrEmpty(carBrand) || !StringUtil.isNullOrEmpty(carModel) || !StringUtil.isNullOrEmpty(carYear) || !StringUtil.isNullOrEmpty(carVersion)) &&
                        (StringUtil.isNullOrEmpty(carBrand) || StringUtil.isNullOrEmpty(carModel) || StringUtil.isNullOrEmpty(carYear) || StringUtil.isNullOrEmpty(carVersion))) {
                    throw new ApiException(ERROR.BAD_REQUEST, "Thông tin cơ bản của xe không hợp lệ");
                }
                request.setOtherCar(otherCar);
            }

            String licensePLate = this.getStringFromRow(row, lastCol++);
            log.info("licensePLate is {}", licensePLate);
            if (licensePLate.isEmpty()) {
                throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin biển số xe");
            }
            if (Boolean.TRUE.equals(this.carRepository.checkExistCarByLicensePlateAndGarageId(licensePLate, garageId, null))) {
                throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với biển số này");
            }
            request.setLicensePLate(licensePLate);

            String vinNumber = this.getStringFromRow(row, lastCol);
            log.info("vinNumber is {}", vinNumber);
            if (!StringUtil.isNullOrEmpty(vinNumber) && Boolean.TRUE.equals(this.carRepository.checkExistCarByVinNumberAndGarageId(vinNumber, garageId, null))) {
                throw new ApiException(ERROR.BAD_REQUEST, "Đã tồn tại thông tin xe với mã số Vin này");
            }
            request.setVinNumber(vinNumber);
        } catch (Exception e) {
            request.setErrors(e.getMessage());
            log.info("error is {}", e.getMessage());
        }
        return request;
    }

    public List<Area> validAddress(String provinceCode, String districtCode, String wardCode)
            throws ApiException {

        PagingDataResponse pageArea = this.areaRepository.findAreaDto(provinceCode, districtCode, wardCode);
        if (pageArea.getData() != null) {
            return (List<Area>) pageArea.getData();
        } else {
            throw new ApiException(ERROR.BAD_REQUEST, "Không tồn tại thông tin địa chỉ");
        }
    }

    public boolean containsValue(XSSFRow row, int fcell, int lcell)
    {
        boolean flag = false;
        for (int i = fcell; i < lcell; i++) {
            if (!StringUtils.isEmpty(String.valueOf(row.getCell(i))) &&
                    !StringUtils.isWhitespace(String.valueOf(row.getCell(i))) &&
                    !StringUtils.isBlank(String.valueOf(row.getCell(i))) &&
                    !String.valueOf(row.getCell(i)).isEmpty() &&
                    row.getCell(i) != null) {
                return true;
            }
        }
        return flag;
    }
}
