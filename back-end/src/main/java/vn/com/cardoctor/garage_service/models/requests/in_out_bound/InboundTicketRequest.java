package vn.com.cardoctor.garage_service.models.requests.in_out_bound;

import lombok.Data;

import java.util.List;

@Data
public class InboundTicketRequest {
    private Integer type;
    private Long distributorId;
    private Long ticketId;
    private String note;
    private List<InboundProductRequest> inboundProducts;
}
