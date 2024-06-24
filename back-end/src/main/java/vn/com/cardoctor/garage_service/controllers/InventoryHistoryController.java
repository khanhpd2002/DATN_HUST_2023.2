package vn.com.cardoctor.garage_service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;
import vn.com.cardoctor.garage_service.models.responses.inventories.InventoryHistoryDetailResponse;
import vn.com.cardoctor.garage_service.services.InventoryHistoryService;

import java.util.Date;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/inventory-histories")
public class InventoryHistoryController {
    @Autowired
    InventoryHistoryService inventoryHistoryService;

    @GetMapping
    public PagingDataResponse findAllInventoryHistoryAdmin(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                           @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.inventoryHistoryService.findAllInventoryHistory(null, fromDate, toDate, pageSize, pageNumber);
    }

    @GetMapping("/{inventory-id}")
    public PagingDataResponse findAllInventoryHistory(@PathVariable(name = "inventory-id") Long inventoryId,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                      @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.inventoryHistoryService.findAllInventoryHistory(inventoryId, fromDate, toDate, pageSize, pageNumber);
    }

    @GetMapping("/{inventory-id}/info/{history-id}")
    public BaseResponse<InventoryHistoryDetailResponse> getDetailHistory(@PathVariable(name = "inventory-id") Long inventoryId,
                                                                         @PathVariable(name = "history-id") Long historyId) throws JsonProcessingException, ApiException {
        BaseResponse<InventoryHistoryDetailResponse> response = new BaseResponse<>();
        response.setData(this.inventoryHistoryService.findHistoryDetail(historyId, false));
        return response;
    }

    @GetMapping("/{inventory-id}/info-to-update-history/{history-id}")
    public BaseResponse<InventoryHistoryDetailResponse> getDetailHistoryToUpdate(@PathVariable(name = "inventory-id") Long inventoryId,
                                                                                 @PathVariable(name = "history-id") Long historyId) throws JsonProcessingException, ApiException {
        BaseResponse<InventoryHistoryDetailResponse> response = new BaseResponse<>();
        response.setData(this.inventoryHistoryService.findHistoryDetail(historyId, true));
        return response;
    }

    @PostMapping("/{inventory-id}")
    public BaseResponse<Long> create(@RequestBody InventoryHistoryRequest inventoryHistoryRequest,
                               @PathVariable(name = "inventory-id") Long inventoryId) throws JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inventoryHistoryService.create(inventoryHistoryRequest, inventoryId));
        return response;
    }

    @PatchMapping("/{inventory-id}/update/{history-id}")
    public BaseResponse<Long> update(@RequestBody InventoryHistoryRequest inventoryHistoryRequest,
                                     @PathVariable(name = "inventory-id") Long inventoryId,
                                     @PathVariable(name = "history-id") Long historyId) throws JsonProcessingException, ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inventoryHistoryService.update(inventoryHistoryRequest, inventoryId, historyId));
        return response;
    }

    @PostMapping("/{inventory-id}/adjustment/{history-id}")
    public BaseResponse<Long> adjustmentQuantity(@RequestBody InventoryHistoryRequest inventoryHistoryRequest,
                                                 @PathVariable(name = "inventory-id") Long inventoryId,
                                                 @PathVariable(name = "history-id") Long historyId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inventoryHistoryService.adjustmentQuantity(inventoryHistoryRequest, inventoryId, historyId));
        return response;
    }

}
