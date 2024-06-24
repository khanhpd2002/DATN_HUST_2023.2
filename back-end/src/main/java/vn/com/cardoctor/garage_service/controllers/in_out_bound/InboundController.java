package vn.com.cardoctor.garage_service.controllers.in_out_bound;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.InboundTicketRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.OutboundTicketRequest;
import vn.com.cardoctor.garage_service.models.responses.in_out_bound.InboundTicketResponse;
import vn.com.cardoctor.garage_service.models.responses.in_out_bound.OutboundTicketResponse;
import vn.com.cardoctor.garage_service.services.in_out_bounds.InboundService;

import java.util.Date;

@RestController
@RequestMapping("/inbounds")
public class InboundController {
    private final InboundService inboundService;

    public InboundController(InboundService inboundService) {
        this.inboundService = inboundService;
    }

    @GetMapping("/{garage-id}")
    public PagingDataResponse findAllInboundTicket(@PathVariable(name = "garage-id") Long garageId,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
                                                   @RequestParam(required = false) Integer type,
                                                   @RequestParam(required = false) String code,
                                                   @RequestParam(required = false) Integer status,
                                                   @RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        return this.inboundService.findAllInboundTicket(garageId, fromDate, toDate, type, code, status, pageSize, pageNumber);
    }

    @GetMapping("/{garage-id}/info/{inbound-ticket-id}")
    public BaseResponse<InboundTicketResponse> getDetailOutboundTicket(@PathVariable(name = "garage-id") Long garageId,
                                                                        @PathVariable(name = "inbound-ticket-id") Long inboundTicketId) throws ApiException {
        BaseResponse<InboundTicketResponse> response = new BaseResponse<>();
        response.setData(this.inboundService.getDetailInboundTicket(inboundTicketId));
        return response;
    }

    @PostMapping("/{garage-id}")
    public BaseResponse<Long> create(@PathVariable(name = "garage-id") Long garageId,
                                     @RequestBody InboundTicketRequest inboundTicketRequest) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inboundService.create(garageId, inboundTicketRequest));
        return response;
    }

    @PatchMapping("/{garage-id}/confirm/{inbound-ticket-id}")
    public BaseResponse<Long> confirmAction(@PathVariable(name = "garage-id") Long garageId,
                                            @PathVariable(name = "inbound-ticket-id") Long inboundTicketId,
                                            @RequestBody InboundTicketRequest request) throws ApiException, JsonProcessingException {
        BaseResponse<Long> response = new BaseResponse<>();
        response.setData(this.inboundService.confirmAction(garageId, inboundTicketId, request));
        return response;
    }
}
