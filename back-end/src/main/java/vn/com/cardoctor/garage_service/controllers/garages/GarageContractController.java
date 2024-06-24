package vn.com.cardoctor.garage_service.controllers.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.GarageContract;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageContractRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageContractResponse;
import vn.com.cardoctor.garage_service.services.garages.GarageContractService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/garage-contracts")
public class GarageContractController {
    @Autowired
    GarageContractService garageContractService;

    @GetMapping
    public PagingDataResponse getAllGarageContract(@RequestParam(required = false) String garageName,
                                                   @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.garageContractService.findAllGarageContract(garageName, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<GarageContractResponse> getGarageContractById(@PathVariable(name = "id") Long id) throws ApiException {
        return this.garageContractService.findById(id);
    }

    @PostMapping
    public BaseResponse create(@RequestBody GarageContractRequest garageContractRequest) {
        return this.garageContractService.create(garageContractRequest);
    }

}
