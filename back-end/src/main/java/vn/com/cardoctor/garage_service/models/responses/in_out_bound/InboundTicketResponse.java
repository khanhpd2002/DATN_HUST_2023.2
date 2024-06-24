package vn.com.cardoctor.garage_service.models.responses.in_out_bound;

import lombok.Data;
import vn.com.cardoctor.garage_service.entities.InboundProduct;
import vn.com.cardoctor.garage_service.entities.OutboundProduct;

import java.util.Date;
import java.util.List;

@Data
public class InboundTicketResponse {
    private Long id;
    private Integer type;
    private String code;
    private Date createdAt;
    private Long ticketId;
    private Integer status;
    private Long distributorId;
    private List<InboundProduct> inboundProducts;
}