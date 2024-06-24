package vn.com.cardoctor.garage_service.controllers.garages;

import lombok.RequiredArgsConstructor;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageServiceRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageServiceResponse;
import vn.com.cardoctor.garage_service.services.garages.GarageServiceService;

import java.util.List;

@RestController
@RequestMapping("/garage-services")
@RequiredArgsConstructor
public class GarageServiceController {
    private final GarageServiceService garageServiceService;

    @GetMapping("/apply-all")
    public PagingDataResponse getServiceApplyForAllGarage(@RequestParam(required = false) String name,
                                                          @RequestParam(required = false) String code,
                                                          @RequestParam(required = false) Long garageServiceTypeId,
                                                          @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.garageServiceService.getServiceApplyForAllGarage(name, code, garageServiceTypeId, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAll(@PathVariable(name = "garage-id") Long garageId,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String code,
                                      @RequestParam(required = false) Long garageServiceTypeId,
                                      @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.garageServiceService.findAll(garageId, name, code, garageServiceTypeId, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{id}")
    public BaseResponse<GarageService> findById(@PathVariable(name = "garage-id") Long garageId,
                                              @PathVariable(name = "id") Long id) throws ApiException {
        BaseResponse<GarageService> response = new BaseResponse<>();
        response.setData(this.garageServiceService.findById(garageId, id));
        return response;
    }

    @PostMapping("/{garage-id}/bulk")
    public BaseResponse<List<GarageServiceResponse>> bulkCreate(@PathVariable(name = "garage-id") Long garageId,
                                                                @RequestBody List<GarageServiceRequest> requests) throws ApiException {
        BaseResponse<List<GarageServiceResponse>> response = new BaseResponse<>();
        response.setData(this.garageServiceService.bulkCreate(garageId, requests));
        return response;
    }

    @PostMapping("/apply-all")
    public BaseResponse<GarageServiceResponse> createForAllGarage(@RequestBody GarageServiceRequest request) throws ApiException {
        BaseResponse<GarageServiceResponse> response = new BaseResponse<>();
        response.setData(this.garageServiceService.create(null, request));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<GarageServiceResponse> create(@PathVariable(name = "garage-id") Long garageId,
                                                      @RequestBody GarageServiceRequest request) throws ApiException {
        BaseResponse<GarageServiceResponse> response = new BaseResponse<>();
        response.setData(this.garageServiceService.create(garageId, request));
        return response;
    }

    @PatchMapping("/{garage-id}/update/{garage-service-id}")
    public BaseResponse<Long> update(@PathVariable(name = "garage-id") Long garageId,
                                     @PathVariable(name = "garage-service-id") Long garageServiceId,
                                     @RequestBody GarageServiceRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.garageServiceService.update(garageId, garageServiceId, request).getId());
        return response;
    }
}
