package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import model.PagingDataResponse;

import java.math.BigDecimal;
import java.util.Date;

public interface InboundTicketRepositoryCustom {
    public PagingDataResponse findAllInboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                                   Integer pageSize, Integer pageNumber);

    BigDecimal getNumberTicketRefundRate(Long garageId, String fromDate, String toDate);
}
