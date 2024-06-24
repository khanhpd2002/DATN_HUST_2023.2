package vn.com.cardoctor.garage_service.controllers.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.GarageGroup;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageGroupRequest;
import vn.com.cardoctor.garage_service.services.garages.GarageGroupService;
import vn.com.cardoctor.garage_service.services.garages.GarageService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/garages-group")
public class GarageGroupController {
    @Autowired
    GarageGroupService garageGroupService;

    @Autowired
    GarageService garageService;

    @GetMapping
    public PagingDataResponse findGarageGroup(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String phone,
                                              @RequestParam(required = false) String website,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) String taxCode,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.garageGroupService.findGarageGroup(name, phone, website, email, taxCode,
                status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<GarageGroup> findById(@PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<GarageGroup> response = new BaseResponse<>();
        response.setData(this.garageGroupService.findById(id));
        return response;
    }

    @PostMapping
    public BaseResponse<Long> createGarageGroup(@RequestBody GarageGroupRequest garageGroupRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageGroupService.createGarageGroup(garageGroupRequest));
        return response;
    }

    @PatchMapping("/{id}")
    public BaseResponse<Long> updateGarageGroup(@RequestBody GarageGroupRequest garageGroupRequest,
                                                @PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageGroupService.updateGarageGroup(garageGroupRequest, id));
        return response;
    }

    @PostMapping("/accept")
    public BaseResponse<String> acceptList(@RequestBody List<Long> ids) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageGroupService.acceptList(ids));
        return response;
    }

    @PostMapping("/reject")
    public BaseResponse<String> rejectList(@RequestBody List<Long> ids) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageGroupService.rejectList(ids));
        return response;
    }
}
