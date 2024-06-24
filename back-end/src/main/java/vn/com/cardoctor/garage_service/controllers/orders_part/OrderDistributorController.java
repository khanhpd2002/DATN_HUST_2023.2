package vn.com.cardoctor.garage_service.controllers.orders_part;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorRequest;
import vn.com.cardoctor.garage_service.models.responses.orders_part.OrderDistributorResponse;
import vn.com.cardoctor.garage_service.services.orders_part.OrderDistributorService;

import java.util.Date;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/orders-distributor")
public class OrderDistributorController {
    @Autowired
    OrderDistributorService orderDistributorService;

    @GetMapping
    public PagingDataResponse findAllOrderDistributor(@RequestParam(required = false) Long garageId,
                                                      @RequestParam(required = false) String orderCode,
                                                      @RequestParam(required = false) Long distributorId,
                                                      @RequestParam(required = false) String distributorCode,
                                                      @RequestParam(required = false) String distributorName,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                      @RequestParam(required = false) Integer deliveryStatus,
                                                      @RequestParam(required = false) Integer paymentStatus,
                                                      @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.orderDistributorService.findAllOrderDistributor(garageId, orderCode, distributorId, distributorCode, distributorName, fromDate, toDate,
                deliveryStatus, paymentStatus, pageSize, pageNumber);
    }

    @GetMapping("/info/{order-distributor-id}")
    public BaseResponse<OrderDistributorResponse> findById(@PathVariable(name = "order-distributor-id") Long orderDistributorId) throws ApiException, JsonProcessingException {
        BaseResponse<OrderDistributorResponse> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.findById(orderDistributorId));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@RequestBody OrderDistributorRequest orderDistributorRequest,
                                     @PathVariable(name = "garage-id") Long garageId,
                                     @RequestParam Integer discountType) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.create(orderDistributorRequest, garageId, discountType));
        return response;
    }

    @PatchMapping("/update/{order-distributor-id}")
    public BaseResponse<Long> update(@PathVariable(name = "order-distributor-id") Long orderDistributorId,
                                     @RequestBody OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.update(orderDistributorId, orderDistributorRequest));
        return response;
    }

    @PatchMapping("/update-ticket/{order-distributor-id}/handle")
    public BaseResponse<Long> handleTicketOrderDistributor(@PathVariable(name = "order-distributor-id") Long orderDistributorId,
                                                           @RequestBody OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.handleTicketOrderDistributor(orderDistributorId, orderDistributorRequest));
        return response;
    }

    @PatchMapping("/{order-distributor-id}/change-payment-status")
    public BaseResponse<Long> changePaymentStatus(@PathVariable("order-distributor-id") Long orderDistributorId,
                                                  @RequestParam("paymentStatus") int paymentStatus) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.changePaymentStatus(orderDistributorId, paymentStatus));
        return response;
    }

    @PatchMapping("/update-ticket/{order-distributor-id}/delivery")
    public BaseResponse<Long> deliveryTicketOrderDistributor(@PathVariable("order-distributor-id") Long orderDistributorId,
                                                             @RequestBody OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(orderDistributorService.deliveryTicketOrderDistributor(orderDistributorId, orderDistributorRequest));
        return response;
    }

    @PatchMapping("/update-ticket/{order-distributor-id}/confirm-receive")
    public BaseResponse<Long> confirmReceiveTicketOrderDistributor(@PathVariable("order-distributor-id") Long orderDistributorId,
                                                                   @RequestBody OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(orderDistributorService.confirmReceiveTicketOrderDistributor(orderDistributorId, orderDistributorRequest));
        return response;
    }

    @PatchMapping("/update-ticket/{order-distributor-id}/cancel")
    public BaseResponse<Long> cancelTicketOrderDistributor(@PathVariable("order-distributor-id") Long orderDistributorId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(orderDistributorService.cancelTicketOrderDistributor(orderDistributorId));
        return response;
    }

    @PatchMapping("/update-ticket/{order-distributor-id}/refund")
    public BaseResponse<Long> refundTicketOrderDistributor(@PathVariable("order-distributor-id") Long orderDistributorId) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.orderDistributorService.refundTicketOrderDistributor(orderDistributorId));
        return response;
    }
}
