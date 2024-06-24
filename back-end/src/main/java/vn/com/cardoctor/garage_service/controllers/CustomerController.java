package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.customer.CustomerRequest;
import vn.com.cardoctor.garage_service.models.responses.customer.CustomerResponse;
import vn.com.cardoctor.garage_service.services.CustomerService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllCustomer(@PathVariable(name = "garage-id") Long garageId,
                                              @RequestParam(required = false) Long customerTypeId,
                                              @RequestParam(required = false) String customerName,
                                              @RequestParam(required = false) String phoneNumber,
                                              @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.customerService.findAllCustomer(garageId, customerTypeId, customerName, phoneNumber, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{customer-id}")
    public BaseResponse<CustomerResponse> findById(@PathVariable(name = "garage-id") Long garageId,
                                                   @PathVariable(name = "customer-id") Long customerId) throws ApiException {
        return this.customerService.findById(garageId, customerId);
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                               @RequestBody CustomerRequest customerRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.customerService.create(customerRequest, garageId));
        return response;
    }

    @PatchMapping("/{garage-id}/update/{customer-id}")
    public BaseResponse<Long> update(@PathVariable(name = "garage-id") Long garageId,
                               @RequestBody CustomerRequest customerRequest,
                               @PathVariable(name = "customer-id") Long customerId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.customerService.update(customerRequest, garageId, customerId));
        return response;
    }
}
