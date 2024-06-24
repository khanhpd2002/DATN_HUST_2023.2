package vn.com.cardoctor.garage_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.enums.ProductTypeEnum;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductHistoryDto;
import vn.com.cardoctor.garage_service.models.requests.product.ProductCompatibilityRequest;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.models.responses.product.CreateProductResponse;
import vn.com.cardoctor.garage_service.models.responses.product.ProductResponse;
import vn.com.cardoctor.garage_service.repositories.*;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCompatibilityRepository productCompatibilityRepository;

    @Autowired
    ConfigPriceRepository configPriceRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    CustomerTypeRepository customerTypeRepository;

    @Autowired
    ObjectMapper objectMapper;

    public PagingDataResponse findAllProduct(Integer sparePartType, String code, String name, Long carBrandId, Long carModelId, Long carYearId, Long carVersionId,
                                             Long distributorId, Long inventoryId, Long garageId, Integer pageSize, Integer pageNumber) {
        return this.productRepository.findAllProduct(sparePartType, code, name, carBrandId, carModelId, carYearId, carVersionId,
                distributorId, inventoryId, garageId, pageSize, pageNumber);
    }

    public PagingDataResponse findAllParentProduct(Long inventoryId, Long distributorId, Integer sparePartType, String code, String name, Integer pageSize, Integer pageNumber) {
        return this.productRepository.findAllParentProduct(inventoryId, distributorId, sparePartType, code, name, pageSize, pageNumber);
    }

    public ProductResponse findParentProductById(Long inventoryId, Long productId) throws ApiException {
        Optional<Product> oProduct = this.productRepository.findById(productId);
        if (oProduct.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Product product = oProduct.get();
        ProductResponse response = this.objectMapper.convertValue(product, ProductResponse.class);
        List<ProductCompatibility> productCompatibilities = this.productCompatibilityRepository.findAllByProductId(productId);
        response.setProductCompatibilities(productCompatibilities);
        return response;
    }

    public Product findParentProductByCode(Long inventoryId, String productCode) throws ApiException {
        Optional<Product> oProduct = this.productRepository.findByCodeAndParentProductIdIsNullAndInventoryId(productCode, inventoryId);
        if (oProduct.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        return oProduct.get();
    }

    public ProductResponse findById(Long inventoryId, Long productId) throws ApiException {
        ProductDto productDto = this.productRepository.findProductById(productId);
        ProductResponse response = this.objectMapper.convertValue(productDto, ProductResponse.class);
        List<ProductCompatibility> productCompatibilities = this.productCompatibilityRepository.findAllByProductId(productDto.getParentProductId());
        response.setProductCompatibilities(productCompatibilities);
        return response;
    }

    public CreateProductResponse create(ProductRequest productRequest, Long inventoryId) throws ApiException {
        this.validRequest(productRequest);
        Optional<Product> oProduct = this.productRepository.findByCodeAndParentProductIdIsNullAndInventoryId(productRequest.getCode(), inventoryId);
        Optional<Inventory> oInventory = this.inventoryRepository.findById(inventoryId);
        if (oInventory.isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Sai thông tin kho chứa");
        }
        Inventory inventory = oInventory.get();
        Product product;
        Long parentProductId;
        if (oProduct.isEmpty()) {
            Product parentProduct = new Product();
            this.convertRequestToEntity(parentProduct, productRequest);
            parentProduct.setInventoryId(inventoryId);
            parentProduct.setGarageId(inventory.getGarageId());
            parentProduct.setQuantity(BigDecimal.valueOf(0));
            parentProduct.setDistributorId(0L);
            parentProduct = this.productRepository.save(parentProduct);
            parentProductId = parentProduct.getId();
            product = new Product();
            product.setParentProductId(parentProductId);
            product.setCode(productRequest.getCode());
            product.setName(productRequest.getName());
            product.setDistributorId(productRequest.getDistributorId());
            product.setPurchasePrice(productRequest.getPurchasePrice());
            this.productRepository.save(product);
            List<ConfigPrice> configPrices = new ArrayList<>();
            List<CustomerType> customerTypes = this.customerTypeRepository.findByGarageIdIsNull();
            for (CustomerType customerType : customerTypes) {
                ConfigPrice configPrice = new ConfigPrice();
                configPrice.setProductId(parentProductId);
                configPrice.setCustomerTypeId(customerType.getId());
                configPrice.setPrice(BigDecimal.valueOf(0));
                configPrice.setGarageId(inventory.getGarageId());
                configPrice.setType(ProductTypeEnum.SPARE_PART.getCode());
                configPrices.add(configPrice);
            }
            this.configPriceRepository.saveAll(configPrices);
        } else {
            throw new ApiException(ERROR.INVALID_REQUEST, "Mã phụ tùng đã tồn tại với NPP này");
        }
        List<ProductCompatibility> productCompatibilities = new ArrayList<>();
        if (productRequest.getProductCompatibilities() != null) {
            for (ProductCompatibilityRequest productCompatibilityRequest : productRequest.getProductCompatibilities()) {
                ProductCompatibility productCompatibility = getProductCompatibility(productCompatibilityRequest, parentProductId);
                productCompatibilities.add(productCompatibility);
            }
        }
        this.productCompatibilityRepository.saveAll(productCompatibilities);
        CreateProductResponse response = new CreateProductResponse();
        response.setProductId(product.getId());
        response.setParentProductId(parentProductId);
        return response;
    }

    public Long update(ProductRequest productRequest, Long inventoryId, Long productId) throws ApiException {
        this.validRequest(productRequest);
        Optional<Product> oProduct = this.productRepository.findById(productId);
        if (oProduct.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin sản phẩm");
        }
        Optional<Product> oParentProduct = this.productRepository.findById(oProduct.get().getParentProductId());
        if (oParentProduct.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin sản phẩm");
        }
        Optional<Inventory> oInventory = this.inventoryRepository.findById(inventoryId);
        if (oInventory.isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Sai thông tin kho chứa");
        }
        Product parentProduct = oParentProduct.get();
        Product product = oProduct.get();
        this.convertRequestToEntity(parentProduct, productRequest);
        product.setDistributorId(productRequest.getDistributorId());
        this.productRepository.save(parentProduct);
        product.setPurchasePrice(productRequest.getPurchasePrice());
        this.productRepository.save(product);

        this.productCompatibilityRepository.deleteAllByProductId(parentProduct.getId());
        List<ProductCompatibility> productCompatibilities = new ArrayList<>();
        for (ProductCompatibilityRequest productCompatibilityRequest : productRequest.getProductCompatibilities()) {
            ProductCompatibility productCompatibility = getProductCompatibility(productCompatibilityRequest, parentProduct.getId());
            productCompatibilities.add(productCompatibility);
        }
        this.productCompatibilityRepository.saveAll(productCompatibilities);
        return productId;
    }

    public Long updateParentProduct(ProductRequest productRequest, Long inventoryId, Long productId) throws ApiException {
        if (Objects.isNull(productRequest.getQuantity())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Số lượng không được để trống");
        }
        Optional<Product> oParentProduct = this.productRepository.findById(productId);
        if (oParentProduct.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin sản phẩm");
        }
        Product parentProduct = oParentProduct.get();
        this.convertRequestToEntity(parentProduct, productRequest);
        parentProduct.setQuantity(productRequest.getQuantity());
        this.productRepository.save(parentProduct);
        return productId;
    }

    public List<ProductHistoryDto> getAveragePriceProduct(Long inventoryId) {
        return this.productRepository.getAveragePriceProduct(inventoryId);
    }

    private static ProductCompatibility getProductCompatibility(ProductCompatibilityRequest productCompatibilityRequest, Long parentProductId) {
        ProductCompatibility productCompatibility = new ProductCompatibility();
        productCompatibility.setProductId(parentProductId);
        productCompatibility.setCarBrandId(productCompatibilityRequest.getCarBrandId());
        productCompatibility.setCarModelId(productCompatibilityRequest.getCarModelId());
        productCompatibility.setCarYearId(productCompatibilityRequest.getCarYearId());
        productCompatibility.setCarVersionId(productCompatibilityRequest.getCarVersionId());
        return productCompatibility;
    }

    public void convertRequestToEntity(Product product, ProductRequest productRequest) {
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setSparePartType(productRequest.getSparePartType());
        product.setUnit(productRequest.getUnit());
    }

    public void validRequest(ProductRequest productRequest) throws ApiException {
        if (StringUtil.isNullOrEmpty(productRequest.getCode())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin mã sản phẩm");
        }
        if (StringUtil.isNullOrEmpty(productRequest.getName())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin tên sản phẩm");
        }
        if (StringUtil.isNullOrEmpty(productRequest.getUnit())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin đơn vị tính");
        }
    }
}
