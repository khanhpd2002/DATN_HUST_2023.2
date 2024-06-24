package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.Distributor;
import vn.com.cardoctor.garage_service.models.requests.distributor.DistributorRequest;
import vn.com.cardoctor.garage_service.services.DistributorService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/distributors")
public class DistributorController {
    @Autowired
    DistributorService distributorService;

    @GetMapping
    public PagingDataResponse findAllDistributorAdmin(@RequestParam(required = false) String code,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Long provinceId,
                                                 @RequestParam(required = false) Long districtId,
                                                 @RequestParam(required = false) Long wardId,
                                                 @RequestParam(required = false) String address,
                                                 @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.distributorService.findAllDistributor(code, name, provinceId, districtId, wardId, address, null, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllDistributor(@RequestParam(required = false) String code,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Long provinceId,
                                                 @RequestParam(required = false) Long districtId,
                                                 @RequestParam(required = false) Long wardId,
                                                 @RequestParam(required = false) String address,
                                                 @RequestParam Integer pageSize, @RequestParam Integer pageNumber,
                                                 @PathVariable(name = "garage-id") Long garageId) {
        return this.distributorService.findAllDistributor(code, name, provinceId, districtId, wardId, address, garageId, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{distributor-id}")
    public BaseResponse<Distributor> findById(@PathVariable(name = "garage-id") Long garageId,
                                              @PathVariable(name = "distributor-id") Long distributorId) throws ApiException {
        return this.distributorService.findById(distributorId);
    }

    @PostMapping("/{garage-id}")
    public BaseResponse create(@PathVariable(name = "garage-id") Long garageId,
                               @RequestBody DistributorRequest distributorRequest) throws ApiException {
        return this.distributorService.create(distributorRequest, garageId);
    }

    @PatchMapping("/{garage-id}/update/{distributor-id}")
    public BaseResponse create(@PathVariable(name = "garage-id") Long garageId,
                               @RequestBody DistributorRequest distributorRequest,
                               @PathVariable(name = "distributor-id") Long distributorId) throws ApiException {
        return this.distributorService.update(distributorRequest, garageId, distributorId);
    }

    @DeleteMapping("/{garage-id}/delete/{distributor-id}")
    public BaseResponse create(@PathVariable(name = "garage-id") Long garageId,
                               @PathVariable(name = "distributor-id") Long distributorId) throws ApiException {
        return this.distributorService.delete(garageId, distributorId);
    }
}
