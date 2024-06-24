package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.dtos.CarInfoDetailDto;
import vn.com.cardoctor.garage_service.models.dtos.HistoryRepairOrderDto;
import vn.com.cardoctor.garage_service.models.requests.car.CarRequest;
import vn.com.cardoctor.garage_service.services.CarService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/cars")
public class CarController {
    @Autowired
    CarService carService;

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllCar(@RequestParam(required = false) Long carBrandId,
                                         @RequestParam(required = false) Long carModelId,
                                         @RequestParam(required = false) Long carYearId,
                                         @RequestParam(required = false) Long carVersionId,
                                         @RequestParam(required = false) String licensePlate,
                                         @PathVariable(name = "garage-id") Long garageId,
                                         @RequestParam(required = false) Long customerTypeId,
                                         @RequestParam(required = false) Long customerId,
                                         @RequestParam(required = false) String filter,
                                         @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.carService.findAllCar(carBrandId, carModelId, carYearId, carVersionId, licensePlate,
                garageId, customerTypeId, customerId, filter, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{car-id}")
    public BaseResponse<CarInfoDetailDto> findById(@PathVariable(name = "garage-id") Long garageId,
                                                   @PathVariable(name = "car-id") Long carId) throws ApiException {
        return this.carService.findById(garageId, carId);
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody CarRequest carRequest) throws ApiException {
        BaseResponse response = new BaseResponse<>();
        Long id = this.carService.create(garageId, carRequest);
        response.setData(id);
        return response;
    }

    @PatchMapping("/{garage-id}/update/{car-id}")
    public BaseResponse update(@PathVariable(name = "garage-id") Long garageId,
                               @PathVariable(name = "car-id") Long carId,
                               @RequestBody CarRequest carRequest) throws ApiException {
        return this.carService.update(garageId, carRequest, carId);
    }

    @PatchMapping("/update-basic-info/{car-id}")
    public BaseResponse<Long> update(@PathVariable(name = "car-id") Long carId,
                               @RequestBody CarRequest carRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.carService.updateBasicInfoCar(carId, carRequest));
        return response;
    }

    @GetMapping("/{car-id}/history-repair-order")
    public BaseResponse<List<HistoryRepairOrderDto>> findAllHistoryRepairOrder(@PathVariable(name = "car-id") Long carId) {
        BaseResponse<List<HistoryRepairOrderDto>> response = new BaseResponse<>();
        response.setData(this.carService.findAllHistoryRepairOrder(carId));
        return response;
    }

}
