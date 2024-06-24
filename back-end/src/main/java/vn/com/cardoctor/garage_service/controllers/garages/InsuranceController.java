package vn.com.cardoctor.garage_service.controllers.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.Insurance;
import vn.com.cardoctor.garage_service.models.requests.insurance.InsuranceRequest;
import vn.com.cardoctor.garage_service.services.garages.InsuranceService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/insurances")
public class InsuranceController {
    @Autowired
    InsuranceService insuranceService;

    @GetMapping
    public PagingDataResponse findAllInsurance(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.insuranceService.findAllInsurance(name, description, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<Insurance> findById(@PathVariable(name = "id") Long id) throws ApiException {
        return this.insuranceService.findById(id);
    }

    @PostMapping
    public BaseResponse create(@RequestBody InsuranceRequest insuranceRequest) throws ApiException {
        return this.insuranceService.create(insuranceRequest);
    }

    @PatchMapping("/{id}")
    public BaseResponse update(@RequestBody InsuranceRequest insuranceRequest, @PathVariable(name = "id") Long id) throws ApiException {
        return this.insuranceService.update(insuranceRequest, id);
    }

    @PostMapping("/accept")
    public BaseResponse acceptList(@RequestBody List<Long> ids) throws ApiException {
        return this.insuranceService.acceptList(ids);
    }

    @PostMapping("/reject")
    public BaseResponse rejectList(@RequestBody List<Long> ids) throws ApiException {
        return this.insuranceService.rejectList(ids);
    }
}
