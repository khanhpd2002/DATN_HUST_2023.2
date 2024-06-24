package vn.com.cardoctor.garage_service.controllers.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationGmsRequest;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationRequest;
import vn.com.cardoctor.garage_service.models.responses.quotation.QuotationResponse;
import vn.com.cardoctor.garage_service.services.orders.QuotationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/repair-orders/{repair-order-id}/quotations")
public class QuotationController {

    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping
    public BaseResponse<Long> createQuotationGms(@PathVariable("repair-order-id") Long repairOrderId,
                                                 @RequestBody QuotationGmsRequest request,
                                                 @RequestParam boolean isStartRepair,
                                                 @RequestParam Integer discountType) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        Long id = quotationService.createQuotationGms(repairOrderId, request, isStartRepair, discountType);
        response.setData(id);
        return response;
    }

    @PatchMapping("/{id}")
    public BaseResponse<Long> updateQuotationGms(@PathVariable("repair-order-id") Long repairOrderId,
                                                 @PathVariable("id") Long id,
                                                 @RequestBody QuotationGmsRequest request,
                                                 @RequestParam boolean isStartRepair) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(quotationService.updateQuotationGms(repairOrderId, id, request, isStartRepair));
        return response;
    }

    @PatchMapping("/{id}/change-status")
    public BaseResponse<Long> changeQuotationStatus(@PathVariable("repair-order-id") Long repairOrderId,
                                              @PathVariable("id") Long id,
                                              @RequestParam Integer quotationStatus, HttpServletRequest httpServletRequest) throws ApiException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(quotationService.changeQuotationStatus(repairOrderId, id, quotationStatus, httpServletRequest));
        return response;
    }

    @GetMapping("/info/{quotation-id}")
    public BaseResponse<QuotationResponse> detailQuotation(@PathVariable("repair-order-id") Long repairOrderId,
                                                           @PathVariable("quotation-id") Long quotationId) throws ApiException {
        return quotationService.detailQuotation(repairOrderId, quotationId);
    }
}
