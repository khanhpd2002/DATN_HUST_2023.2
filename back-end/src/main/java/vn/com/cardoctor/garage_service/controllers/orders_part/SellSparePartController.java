package vn.com.cardoctor.garage_service.controllers.orders_part;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartFilter;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartRequest;
import vn.com.cardoctor.garage_service.models.responses.orders_part.SellSparePartResponse;
import vn.com.cardoctor.garage_service.services.orders_part.SellSparePartService;

import java.util.Date;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/sell-spare-parts")
public class SellSparePartController {
    @Autowired
    SellSparePartService sellSparePartService;

    @GetMapping
    public PagingDataResponse findAllSellSparePart(@RequestParam(required = false) Long garageId,
                                                   @RequestParam(required = false) Long customerTypeId,
                                                   @RequestParam(required = false) String customerPhone,
                                                   @RequestParam(required = false) String customerName,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                   @RequestParam(required = false) String  filter,
                                                   @RequestParam(required = false) Integer deliveryStatus,
                                                   @RequestParam(required = false) Integer paymentStatus,
                                                   @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        SellSparePartFilter sparePartFilter = SellSparePartFilter.builder()
                .customerTypeId(customerTypeId)
                .customerPhone(customerPhone)
                .customerName(customerName)
                .fromDate(fromDate)
                .toDate(toDate)
                .filter(filter)
                .deliveryStatus(deliveryStatus)
                .paymentStatus(paymentStatus)
                .build();
        return this.sellSparePartService.findAllSellSparePart(garageId, sparePartFilter, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllSellSparePartByGarageId(@PathVariable(name = "garage-id") Long garageId,
                                                             @RequestParam(required = false) Long customerTypeId,
                                                             @RequestParam(required = false) String customerPhone,
                                                             @RequestParam(required = false) String customerName,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                             @RequestParam(required = false) String filter,
                                                             @RequestParam(required = false) Integer deliveryStatus,
                                                             @RequestParam(required = false) Integer paymentStatus,
                                                             @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        SellSparePartFilter sparePartFilter = SellSparePartFilter.builder()
                .customerTypeId(customerTypeId)
                .customerPhone(customerPhone)
                .customerName(customerName)
                .fromDate(fromDate)
                .toDate(toDate)
                .filter(filter)
                .deliveryStatus(deliveryStatus)
                .paymentStatus(paymentStatus)
                .build();
        return this.sellSparePartService.findAllSellSparePart(garageId, sparePartFilter, pageSize, pageNumber);
    }

    @GetMapping("/info/{sell-spare-part-id}")
    public BaseResponse<SellSparePartResponse> findById(@PathVariable(name = "sell-spare-part-id") Long sellSparePartId) throws ApiException, JsonProcessingException {
        BaseResponse<SellSparePartResponse> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.findById(sellSparePartId));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@RequestBody SellSparePartRequest sellSparePartRequest,
                                     @PathVariable(name = "garage-id") Long garageId,
                                     @RequestParam Integer discountType) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.create(sellSparePartRequest, garageId, discountType));
        return response;
    }

    @PostMapping("/{garage-id}/v2")
    public BaseResponse<Long> createV2(@RequestBody SellSparePartRequest sellSparePartRequest,
                                     @PathVariable(name = "garage-id") Long garageId,
                                     @RequestParam Integer discountType) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.createV2(sellSparePartRequest, garageId, discountType));
        return response;
    }

    @PatchMapping("/update/{sell-spare-part-id}")
    public BaseResponse<Long> update(@PathVariable(name = "sell-spare-part-id") Long sellSparePartId,
                                     @RequestBody SellSparePartRequest sellSparePartRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.update(sellSparePartId, sellSparePartRequest));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/handle")
    public BaseResponse<Long> handleTicketSellSparePart(@PathVariable(name = "sell-spare-part-id") Long sellSparePartId,
                                                        @RequestBody SellSparePartRequest sellSparePartRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.handleTicketSellSparePart(sellSparePartId, sellSparePartRequest));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/handling/update")
    public BaseResponse<Long> updateHandlingTicketSellSparePart(@PathVariable("sell-spare-part-id") Long sellSparePartId,
                                                                @RequestBody SellSparePartRequest sellSparePartRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(sellSparePartService.updateHandlingTicketSellSparePart(sellSparePartId, sellSparePartRequest));
        return response;
    }

    @PatchMapping("/{sell-spare-part-id}/change-payment-status")
    public BaseResponse<Long> changePaymentStatus(@PathVariable("sell-spare-part-id") Long sellSparePartId,
                                                  @RequestParam("paymentStatus") int paymentStatus) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(sellSparePartService.changePaymentStatus(sellSparePartId, paymentStatus));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/delivery")
    public BaseResponse<Long> deliveryTicketSellSparePart(@PathVariable("sell-spare-part-id") Long sellSparePartId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(sellSparePartService.deliveryTicketSellSparePart(sellSparePartId));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/confirm-receive")
    public BaseResponse<Long> confirmReceiveTicketSellSparePart(@PathVariable("sell-spare-part-id") Long sellSparePartId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(sellSparePartService.confirmReceiveTicketSellSparePart(sellSparePartId));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/cancel")
    public BaseResponse<Long> cancelTicketSellSparePart(@PathVariable("sell-spare-part-id") Long sellSparePartId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(sellSparePartService.cancelTicketSellSparePart(sellSparePartId));
        return response;
    }

    @PatchMapping("/update-ticket/{sell-spare-part-id}/refund")
    public BaseResponse<Long> refundTicketSellSparePart(@PathVariable("sell-spare-part-id") Long sellSparePartId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.sellSparePartService.refundTicketSellSparePart(sellSparePartId));
        return response;
    }
}
