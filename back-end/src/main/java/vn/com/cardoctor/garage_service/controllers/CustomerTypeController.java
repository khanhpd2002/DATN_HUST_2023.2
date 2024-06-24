package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.customer.CustomerTypeRequest;
import vn.com.cardoctor.garage_service.services.CustomerTypeService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/customer-types")
public class CustomerTypeController {
    @Autowired
    CustomerTypeService customerTypeService;

    @GetMapping
    public PagingDataResponse findAllCustomerTypeForAll(@RequestParam(required = false) String name,
                                                        @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.customerTypeService.findAllCustomerTypeForAll(name, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllCustomerType(@PathVariable(name = "garage-id") Long garageId,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.customerTypeService.findAllCustomerType(garageId, name, pageSize, pageNumber);
    }

    @PostMapping
    public BaseResponse<Long> createForAll(@RequestBody CustomerTypeRequest customerTypeRequest) {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.customerTypeService.createForAll(customerTypeRequest));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody CustomerTypeRequest customerTypeRequest) {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.customerTypeService.create(garageId, customerTypeRequest));
        return response;
    }

    @PatchMapping("/{garage-id}/update/{customer-type-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody CustomerTypeRequest customerTypeRequest,
                                     @PathVariable(name = "customer-type-id") Long customerTypeId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.customerTypeService.update(garageId, customerTypeRequest, customerTypeId));
        return response;
    }
}
