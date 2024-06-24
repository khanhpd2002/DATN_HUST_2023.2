package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryRequest;
import vn.com.cardoctor.garage_service.services.InventoryService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                               @RequestBody InventoryRequest inventoryRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inventoryService.create(inventoryRequest, garageId));
        return response;
    }

    @GetMapping
    public PagingDataResponse findAllInventoryAdmin(@RequestParam(required = false) Long garageId,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam Integer pageSize, Integer pageNumber) {
        return this.inventoryService.findAllInventory(garageId, name, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllInventory(@PathVariable(name = "garage-id") Long garageId,
                                         @RequestParam(required = false) String name,
                                         @RequestParam Integer pageSize, Integer pageNumber) {
        return this.inventoryService.findAllInventory(garageId, name, pageSize, pageNumber);
    }

    @PostMapping("/create-for-all-garage")
    public BaseResponse<String> createInventoryForAllGarage() {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.inventoryService.createInventoryForAllGarage());
        return response;
    }

    @PostMapping("/create-for-all-garage-from")
    public BaseResponse<String> createInventoryForGarageFrom(@RequestParam Long garageId) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.inventoryService.createInventoryForGarageFrom(garageId));
        return response;
    }
}
