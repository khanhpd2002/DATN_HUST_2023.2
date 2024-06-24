package vn.com.cardoctor.garage_service.controllers.garages;


import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;
import vn.com.cardoctor.garage_service.models.requests.rescue.RescueRequest;
import vn.com.cardoctor.garage_service.services.garages.RescueService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/rescues")
public class RescueController {
    @Autowired
    RescueService rescueService;

    @GetMapping
    public PagingDataResponse findAllRescue(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String description,
                                            @RequestParam(required = false) Integer status,
                                            @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.rescueService.findAllRescue(name, description, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<Rescue> findById(@PathVariable(name = "id") Long id) throws ApiException {
        return this.rescueService.findById(id);
    }

    @PostMapping
    public BaseResponse create(@RequestBody RescueRequest rescueRequest) throws ApiException {
        return this.rescueService.create(rescueRequest);
    }

    @PatchMapping("/{id}")
    public BaseResponse update(@RequestBody RescueRequest rescueRequest, @PathVariable(name = "id") Long id) throws ApiException {
        return this.rescueService.update(rescueRequest, id);
    }

    @PatchMapping("/{id}/accept")
    public BaseResponse accept(@PathVariable(name = "id") Long id) throws ApiException {
        return this.rescueService.accept(id);
    }

    @PostMapping("/accept")
    public BaseResponse acceptList(@RequestBody List<Long> ids) throws ApiException {
        return this.rescueService.acceptList(ids);
    }

    @PatchMapping("/{id}/reject")
    public BaseResponse reject(@PathVariable(name = "id") Long id) throws ApiException {
        return this.rescueService.reject(id);
    }

    @PostMapping("/reject")
    public BaseResponse rejectList(@RequestBody List<Long> ids) throws ApiException {
        return this.rescueService.rejectList(ids);
    }
}
