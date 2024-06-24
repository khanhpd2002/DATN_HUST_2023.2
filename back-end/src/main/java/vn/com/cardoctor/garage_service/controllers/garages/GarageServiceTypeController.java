package vn.com.cardoctor.garage_service.controllers.garages;

import lombok.RequiredArgsConstructor;
import model.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.cardoctor.garage_service.entities.garages.GarageServiceType;
import vn.com.cardoctor.garage_service.services.garages.GarageServiceTypeService;

import java.util.List;

@RestController
@RequestMapping("/garage-service-types")
@RequiredArgsConstructor
public class GarageServiceTypeController {
    private final GarageServiceTypeService garageServiceTypeService;

    @GetMapping("/{garage-id}")
    public BaseResponse<List<GarageServiceType>> findAllByGarageId(@PathVariable(name = "garage-id") Long garageId) {
        BaseResponse<List<GarageServiceType>> response = new BaseResponse<>();
        response.setData(this.garageServiceTypeService.findAllByGarageId(garageId));
        return response;
    }

    @GetMapping("/apply-all")
    public BaseResponse<List<GarageServiceType>> findServiceTypeApplyForAll() {
        BaseResponse<List<GarageServiceType>> response = new BaseResponse<>();
        response.setData(this.garageServiceTypeService.findServiceTypeApplyForAll());
        return response;
    }
}
