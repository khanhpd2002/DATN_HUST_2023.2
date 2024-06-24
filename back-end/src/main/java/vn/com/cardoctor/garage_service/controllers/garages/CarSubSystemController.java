package vn.com.cardoctor.garage_service.controllers.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.car_sub_system.CarSubSystemRequest;
import vn.com.cardoctor.garage_service.models.responses.car_sub_system.CarSubSystemResponse;
import vn.com.cardoctor.garage_service.services.garages.CarSubSystemService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/car-sub-systems")
public class CarSubSystemController {
    @Autowired
    CarSubSystemService carSubSystemService;

    @GetMapping
    public PagingDataResponse findAllCarSubSystem(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.carSubSystemService.findAllCarSubSystem(name, description, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse findById(@PathVariable(name = "id") Long id) throws ApiException {
        CarSubSystemResponse carSubSystemResponse = this.carSubSystemService.findById(id);
        BaseResponse response = new BaseResponse();
        response.setData(carSubSystemResponse);
        return response;
    }

    @PostMapping
    public BaseResponse create(@RequestBody CarSubSystemRequest carSubSystemRequest) throws ApiException {
        return this.carSubSystemService.create(carSubSystemRequest);
    }

    @PatchMapping("/{id}")
    public BaseResponse update(@PathVariable(name = "id") Long id, @RequestBody CarSubSystemRequest carSubSystemRequest) throws ApiException {
        return this.carSubSystemService.update(carSubSystemRequest, id);
    }

    @PatchMapping("/{id}/accept")
    public BaseResponse accept(@PathVariable(name = "id") Long id) throws ApiException {
        return this.carSubSystemService.accept(id);
    }

    @PostMapping("/accept")
    public BaseResponse acceptList(@RequestBody List<Long> ids) throws ApiException {
        return this.carSubSystemService.acceptList(ids);
    }

    @PatchMapping("/{id}/reject")
    public BaseResponse reject(@PathVariable(name = "id") Long id) throws ApiException {
        return this.carSubSystemService.reject(id);
    }

    @PostMapping("/reject")
    public BaseResponse rejectList(@RequestBody List<Long> ids) throws ApiException {
        return this.carSubSystemService.rejectList(ids);
    }
}

