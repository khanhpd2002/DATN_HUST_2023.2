package vn.com.cardoctor.garage_service.controllers.in_out_bound;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.InboundTicketRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.OutboundTicketRequest;
import vn.com.cardoctor.garage_service.models.responses.in_out_bound.OutboundTicketResponse;
import vn.com.cardoctor.garage_service.services.in_out_bounds.OutboundService;

import java.util.Date;

@RestController
@RequestMapping("/outbounds")
public class OutboundController {
    private final OutboundService outboundService;

    public OutboundController(OutboundService outboundService) {
        this.outboundService = outboundService;
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllOutboundTicket(@PathVariable(name = "garage-id") Long garageId,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                     @RequestParam(required = false) Integer type,
                                                     @RequestParam(required = false) String code,
                                                     @RequestParam(required = false) Integer status,
                                                     @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.outboundService.findAllOutboundTicket(garageId, fromDate, toDate, type, code, status, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{outbound-ticket-id}")
    public BaseResponse<OutboundTicketResponse> getDetailOutboundTicket(@PathVariable(name = "garage-id") Long garageId,
                                                                        @PathVariable(name = "outbound-ticket-id") Long outboundTicketId) throws ApiException {
        BaseResponse<OutboundTicketResponse> response = new BaseResponse<>();
        response.setData(this.outboundService.getDetailOutboundTicket(outboundTicketId));
        return response;
    }

    @PatchMapping("/{garage-id}/confirm/{outbound-ticket-id}")
    public BaseResponse<Long> confirmAction(@PathVariable(name = "garage-id") Long garageId,
                                            @PathVariable(name = "outbound-ticket-id") Long outboundTicketId,
                                            @RequestBody OutboundTicketRequest request) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.outboundService.confirmAction(garageId, outboundTicketId, request));
        return response;
    }
}
