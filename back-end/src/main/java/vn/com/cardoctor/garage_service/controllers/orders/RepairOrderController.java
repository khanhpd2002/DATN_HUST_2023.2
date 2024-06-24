package vn.com.cardoctor.garage_service.controllers.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.controllers.BaseController;
import vn.com.cardoctor.garage_service.models.requests.repair_order.FilterRepairOrder;
import vn.com.cardoctor.garage_service.models.requests.repair_order.RepairOrderRequest;
import vn.com.cardoctor.garage_service.models.responses.repair_order.PrintRepairOrderResponse;
import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
import vn.com.cardoctor.garage_service.services.orders.RepairOrderService;

import java.util.Date;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/repair-orders")
public class RepairOrderController extends BaseController {

    @Autowired
    private RepairOrderService repairOrderService;

    @GetMapping("/{garage-id}/info/{repair-order-id}")
    public BaseResponse<RepairOrderResponse> getDetailRepairOrderGms(@PathVariable(name = "garage-id") Long garageId,
                                                                      @PathVariable(name = "repair-order-id") Long repairOrderId) throws ApiException, JsonProcessingException {
        BaseResponse<RepairOrderResponse> response = new BaseResponse<>();
        response.setData(this.repairOrderService.getDetailRepairOrderGms(repairOrderId));
        return response;
    }

    @GetMapping("/{garage-id}/info-to-print/{repair-order-id}")
    public BaseResponse<PrintRepairOrderResponse> getDetailRepairOrderForPrint(@PathVariable(name = "garage-id") Long garageId,
                                                                               @PathVariable(name = "repair-order-id") Long repairOrderId) throws ApiException, JsonProcessingException {
        BaseResponse<PrintRepairOrderResponse> response = new BaseResponse<>();
        response.setData(this.repairOrderService.getDetailRepairOrderForPrint(repairOrderId));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> createRepairOrderGmsOrder(@PathVariable(name = "garage-id") Long garageId,
                                                                    @RequestBody RepairOrderRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.createRepairOrderGms(garageId, request));
        return response;
    }

    @PatchMapping("/{garage-id}/update/{repair-order-id}")
    public BaseResponse<Long> updateRepairOrderGms(@PathVariable(name = "garage-id") Long garageId,
                                                        @PathVariable(name = "repair-order-id") Long repairOrderId,
                                                        @RequestBody RepairOrderRequest request) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.updateRepairOrderGms(garageId, repairOrderId, request));
        return response;
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse getRepairOrdersByGarageId(@PathVariable(name = "garage-id") Long garageId,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                        @RequestParam(required = false) Long status,
                                                        @RequestParam(required = false) Long paymentStatus,
                                                        @RequestParam(required = false) Long customerTypeId,
                                                        @RequestParam(required = false) String customerName,
                                                        @RequestParam(required = false) String filter,
                                                        @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        FilterRepairOrder filterRepairOrder = FilterRepairOrder.builder()
                .garageId(garageId)
                .fromDate(fromDate)
                .toDate(toDate)
                .status(status)
                .paymentStatus(paymentStatus)
                .customerTypeId(customerTypeId)
                .customerName(customerName)
                .filter(filter)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        return repairOrderService.getAllRepairOrders(filterRepairOrder);
    }

    @GetMapping("/admin")
    public PagingDataResponse adminGetAllRepairOrder(@RequestParam(required = false) String code,
                                                     @RequestParam(required = false) String garageOwnerPhone,
                                                     @RequestParam(required = false) String garageName,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                     @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
        return repairOrderService.adminGetAllRepairOrder(code, garageOwnerPhone, garageName, fromDate, toDate, pageNumber, pageSize);
    }

    @PatchMapping("/{repair-order-id}/change-payment-status")
    public BaseResponse<Long> changePaymentStatus(@PathVariable("repair-order-id") Long repairOrderId,
                                                  @RequestParam("paymentStatus") int paymentStatus) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.changePaymentStatus(repairOrderId, paymentStatus));
        return response;
    }

    @PatchMapping("/{repair-order-id}/change-quotation-status")
    public BaseResponse<Long> changeQuotationStatus(@PathVariable("repair-order-id") Long repairOrderId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.changeQuotationStatus(repairOrderId));
        return response;
    }

    @PatchMapping("/{repair-order-id}/complete-repair")
    public BaseResponse<Long> completeRepair(@PathVariable("repair-order-id") Long repairOrderId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.completeRepair(repairOrderId));
        return response;
    }

    @PatchMapping("/{repair-order-id}/handover-car")
    public BaseResponse<Long> handoverCar(@PathVariable("repair-order-id") Long repairOrderId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.handoverCar(repairOrderId));
        return response;
    }

    @PatchMapping("/{repair-order-id}/cancel")
    public BaseResponse<Long> cancel(@PathVariable("repair-order-id") Long repairOrderId) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(repairOrderService.cancel(repairOrderId));
        return response;
    }

}
