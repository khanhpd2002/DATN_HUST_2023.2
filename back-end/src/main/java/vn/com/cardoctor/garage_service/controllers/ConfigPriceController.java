package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;
import vn.com.cardoctor.garage_service.models.requests.product.ConfigPriceRequest;
import vn.com.cardoctor.garage_service.models.responses.product.ConfigPriceResponse;
import vn.com.cardoctor.garage_service.services.ConfigPriceService;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/config-prices")
public class ConfigPriceController {
    @Autowired
    ConfigPriceService configPriceService;

    @GetMapping
    public PagingDataResponse findAllConfigPriceAdmin(@RequestParam(required = false) Long type,
                                                       @RequestParam(required = false) String code,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.configPriceService.findAllConfigPrice(null, type, code, name, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllConfigPrice(@PathVariable(name = "garage-id") Long garageId,
                                                  @RequestParam(required = false) Long type,
                                                  @RequestParam(required = false) String code,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.configPriceService.findAllConfigPrice(garageId, type, code, name, pageSize, pageNumber);
    }

    @GetMapping("/{customer-type-id}/prices")
    public BaseResponse<ConfigPriceDto> findByCustomerTypeAndProductAndService(@PathVariable(name = "customer-type-id") Long customerTypeId,
                                                                               @RequestParam(required = false) Long modelId, @RequestParam Integer type) {
        ConfigPriceDto configPriceDto = this.configPriceService.findByCustomerTypeAndProductAndService(customerTypeId, modelId, type);
        BaseResponse<ConfigPriceDto> response = new BaseResponse<>();
        response.setData(configPriceDto);
        return response;
    }

    @GetMapping("/{garage-id}/info/{product-id}")
    public BaseResponse<ConfigPriceResponse> findById(@PathVariable(name = "garage-id") Long garageId,
                                                      @PathVariable(name = "model-id") Long modelId,
                                                      @RequestParam Integer type) throws ApiException {
        ConfigPriceResponse configPrice = this.configPriceService.findById(garageId, modelId, type);
        BaseResponse<ConfigPriceResponse> response = new BaseResponse<>();
        response.setData(configPrice);
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody ConfigPriceRequest configPriceRequest,
                                     @RequestParam Integer type) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.configPriceService.create(garageId, configPriceRequest, type));
        return response;
    }

    @PatchMapping("/{garage-id}/update/{model-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody ConfigPriceRequest configPriceRequest,
                                     @PathVariable(name = "model-id") Long modelId,
                                     @RequestParam Integer type) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.configPriceService.update(garageId, configPriceRequest, modelId, type));
        return response;
    }
}
