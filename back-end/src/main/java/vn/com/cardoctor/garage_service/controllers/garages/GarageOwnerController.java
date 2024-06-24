package vn.com.cardoctor.garage_service.controllers.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.garage_owner.GarageOwnerRequest;
import vn.com.cardoctor.garage_service.models.responses.garage_owner.CheckExistGarageOwnerResponse;
import vn.com.cardoctor.garage_service.models.responses.garage_owner.GarageOwnerDto;
import vn.com.cardoctor.garage_service.services.garages.GarageOwnerService;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/garage-owners")
public class GarageOwnerController {
    @Autowired
    GarageOwnerService garageOwnerService;

    @GetMapping
    public PagingDataResponse findAll(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String userName,
                                      @RequestParam(required = false) String phone,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(required = false) Integer gender,
                                      @RequestParam(required = false) Date fromDateBirthday,
                                      @RequestParam(required = false) Date toDateBirthday,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.garageOwnerService.findAllGarageOwner(name, userName, phone, email, gender,
                fromDateBirthday, toDateBirthday, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<GarageOwnerDto> findById(@PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<GarageOwnerDto> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.findById(id));
        return response;
    }

    @PostMapping
    public BaseResponse<Long> create(@RequestBody GarageOwnerRequest garageOwnerRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.create(garageOwnerRequest));
        return response;
    }

    @PatchMapping("/{id}")
    public BaseResponse<Long> update(@RequestBody GarageOwnerRequest garageOwnerRequest,
                               @PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.update(garageOwnerRequest, id));
        return response;
    }

    @PatchMapping("/{id}/accept")
    public BaseResponse<Long> accept(@PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.accept(id));
        return response;
    }

    @PostMapping("/accept")
    public BaseResponse<String> acceptList(@RequestBody List<Long> ids) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.acceptList(ids));
        return response;
    }

    @PatchMapping("/{id}/reject")
    public BaseResponse<Long> reject(@PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.reject(id));
        return response;
    }

    @PostMapping("/reject")
    public BaseResponse<String> rejectList(@RequestBody List<Long> ids) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.rejectList(ids));
        return response;
    }

    @PostMapping("/check-exist-by-phone-numbers")
    public BaseResponse<List<CheckExistGarageOwnerResponse>> checkExistGarageOwners(@RequestBody List<String> phoneNumbers) {
        BaseResponse<List<CheckExistGarageOwnerResponse>> response = new BaseResponse<>();
        response.setData(this.garageOwnerService.checkExistGarageOwners(phoneNumbers));
        return response;
    }
}
