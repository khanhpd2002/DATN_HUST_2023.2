package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.models.dtos.ProductHistoryDto;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.models.responses.product.CreateProductResponse;
import vn.com.cardoctor.garage_service.models.responses.product.ProductResponse;
import vn.com.cardoctor.garage_service.services.ProductService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public PagingDataResponse findAllProductAdmin(@RequestParam(required = false) String code,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Integer sparePartType,
                                                  @RequestParam(required = false) Long carBrandId,
                                                  @RequestParam(required = false) Long carModelId,
                                                  @RequestParam(required = false) Long carYearId,
                                                  @RequestParam(required = false) Long carVersionId,
                                                  @RequestParam(required = false) Long distributorId,
                                                  @RequestParam(required = false) Long garageId,
                                                  @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.productService.findAllProduct(sparePartType, code, name, carBrandId, carModelId, carYearId, carVersionId,
                distributorId, null, garageId, pageSize, pageNumber);
    }

    @GetMapping("/{inventory-id}")
    public PagingDataResponse findAllProduct(@PathVariable(name = "inventory-id") Long inventoryId,
                                                @RequestParam(required = false) String code,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) Integer sparePartType,
                                                @RequestParam(required = false) Long carBrandId,
                                                @RequestParam(required = false) Long carModelId,
                                                @RequestParam(required = false) Long carYearId,
                                                @RequestParam(required = false) Long carVersionId,
                                                @RequestParam(required = false) Long distributorId,
                                                @RequestParam(required = false) Long garageId,
                                                @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.productService.findAllProduct(sparePartType, code, name, carBrandId, carModelId, carYearId, carVersionId,
                distributorId, inventoryId, garageId, pageSize, pageNumber);
    }

    @GetMapping("/{inventory-id}/parent")
    public PagingDataResponse findAllParentProduct(@PathVariable(name = "inventory-id") Long inventoryId,
                                                   @RequestParam(required = false) Integer sparePartType,
                                                   @RequestParam(required = false) Long distributorId,
                                                   @RequestParam(required = false) String code,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.productService.findAllParentProduct(inventoryId, distributorId, sparePartType, code, name, pageSize, pageNumber);
    }

    @GetMapping("/{inventory-id}/parent-info/{product-id}")
    public BaseResponse<ProductResponse> findParentProductById(@PathVariable(name = "inventory-id") Long inventoryId,
                                                               @PathVariable(name = "product-id") Long productId) throws ApiException {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setData(this.productService.findParentProductById(inventoryId, productId));
        return response;
    }

    @GetMapping("/{inventory-id}/parent-info-by-code/{product-code}")
    public BaseResponse<Product> findParentProductByCode(@PathVariable(name = "inventory-id") Long inventoryId,
                                                         @PathVariable(name = "product-code") String productCode) throws ApiException {
        BaseResponse<Product> response = new BaseResponse<>();
        response.setData(this.productService.findParentProductByCode(inventoryId, productCode));
        return response;
    }

    @GetMapping("/{inventory-id}/info/{product-id}")
    public BaseResponse<ProductResponse> findById(@PathVariable(name = "inventory-id") Long inventoryId,
                                                  @PathVariable(name = "product-id") Long productId) throws ApiException {
        BaseResponse<ProductResponse> response = new BaseResponse<>();
        response.setData(this.productService.findById(inventoryId, productId));
        return response;
    }

    @PostMapping("/{inventory-id}")
    public BaseResponse<CreateProductResponse> create(@PathVariable(name = "inventory-id") Long inventoryId,
                                                      @RequestBody ProductRequest productRequest) throws ApiException {
        BaseResponse<CreateProductResponse> response = new BaseResponse<>();
        response.setData(this.productService.create(productRequest, inventoryId));
        return response;
    }

    @PatchMapping("/{inventory-id}/update/{product-id}")
    public BaseResponse<Long> update(@PathVariable(name = "inventory-id") Long inventoryId,
                                     @RequestBody ProductRequest productRequest,
                                     @PathVariable(name = "product-id") Long productId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.productService.update(productRequest, inventoryId, productId));
        return response;
    }

    @PatchMapping("/{inventory-id}/update-parent/{product-id}")
    public BaseResponse<Long> updateParentProduct(@PathVariable(name = "inventory-id") Long inventoryId,
                                                  @RequestBody ProductRequest productRequest,
                                                  @PathVariable(name = "product-id") Long productId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.productService.updateParentProduct(productRequest, inventoryId, productId));
        return response;
    }

    @GetMapping("/{inventory-id}/product-histories")
    public BaseResponse<List<ProductHistoryDto>> getAveragePriceProduct(@PathVariable(name = "inventory-id") Long inventoryId) {
        BaseResponse<List<ProductHistoryDto>> response = new BaseResponse<>();
        response.setData(this.productService.getAveragePriceProduct(inventoryId));
        return response;
    }

}
