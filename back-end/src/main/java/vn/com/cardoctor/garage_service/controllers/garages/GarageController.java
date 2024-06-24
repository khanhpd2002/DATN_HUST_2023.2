package vn.com.cardoctor.garage_service.controllers.garages;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageBookingRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.UpdateLogoRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageInfoResponse;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageResponse;
import vn.com.cardoctor.garage_service.services.garages.GarageService;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/garages")
@Api(value = "Sample API")
public class GarageController {
    @Autowired
    GarageService garageService;

    @GetMapping
    @ApiOperation(value = "Get all garage", response = PagingDataResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully", response = PagingDataResponse.class),
            @ApiResponse(code = 404, message = "Not found - Garage was not found"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public PagingDataResponse findAll(@RequestParam(required = false) String code,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String contactPointName,
                                      @RequestParam(required = false) String contactPointPhone,
                                      @RequestParam(required = false) Long provinceId,
                                      @RequestParam(required = false) Long districtId,
                                      @RequestParam(required = false) Long wardId,
                                      @RequestParam(required = false) String address,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) List<Long> carSubSystems,
                                      @RequestParam(required = false) List<Long> rescues,
                                      @RequestParam(required = false) List<Long> insurances,
                                      @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.garageService.findAllGarage(code, name, provinceId, districtId, wardId, address, contactPointName, contactPointPhone,
                status, carSubSystems, rescues, insurances, pageSize, pageNumber);
    }

    @GetMapping("booking")
    public PagingDataResponse searchGarageToBooking(@RequestBody GarageBookingRequest garageBookingRequest,
                                                    @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.garageService.searchGarageToBooking(garageBookingRequest, pageSize, pageNumber);
    }

    @GetMapping("/get-by-owner/{garage-owner-id}")
    public BaseResponse<List<Garage>> findByGarageOwner(@PathVariable(name = "garage-owner-id") Long garageOwnerId) throws ApiException {
        List<Garage> garages = this.garageService.findByGarageOwner(garageOwnerId);
        BaseResponse<List<Garage>> response = new BaseResponse<>();
        response.setData(garages);
        return response;
    }

    @GetMapping("/{id}")
    public BaseResponse<GarageResponse> findById(@PathVariable(name = "id") Long id) throws JsonProcessingException, ApiException {
        BaseResponse<GarageResponse> response = new BaseResponse<>();
        response.setData(this.garageService.findById(id));
        return response;
    }

    @PostMapping
    public BaseResponse<Long> create(@RequestBody GarageRequest garageRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageService.create(garageRequest));
        return response;
    }

    @PatchMapping("/{id}")
    public BaseResponse<Long> update(@RequestBody GarageRequest garageRequest, @PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageService.update(garageRequest, id));
        return response;
    }

    @PostMapping("/accept")
    public BaseResponse<String> acceptList(@RequestBody List<Long> ids) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageService.acceptList(ids));
        return response;
    }

    @PostMapping("/reject")
    public BaseResponse<String> rejectList(@RequestBody List<Long> ids) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.garageService.rejectList(ids));
        return response;
    }

    @PostMapping("/{id}/update-logo")
    public BaseResponse<Long> updateLogo(@PathVariable(name = "id") Long id,
                                         @RequestBody UpdateLogoRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageService.updateLogo(id, request));
        return response;
    }

    @PostMapping("/insert")
    public BaseResponse<String> insertGarage(@RequestBody GarageRequest garageRequest) {
        BaseResponse<String> response = new BaseResponse<>();
        this.garageService.insertGarage(garageRequest);
        response.setData("Success");
        return response;
    }

    @GetMapping("/{id}/garage-info")
    public BaseResponse<GarageInfoResponse> findGarageInfoById(@PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<GarageInfoResponse> response = new BaseResponse<>();
        response.setData(this.garageService.findGarageInfoById(id));
        return response;
    }

    @PatchMapping("/{id}/garage-info")
    public BaseResponse<Long> updateGarageInfo(@PathVariable(name = "id") Long id,
                                               @RequestBody Map<String, Object> additionalInformation) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageService.updateGarageInfo(id, additionalInformation));
        return response;
    }
}
