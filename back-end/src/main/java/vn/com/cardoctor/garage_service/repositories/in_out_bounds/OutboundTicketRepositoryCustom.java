package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import model.PagingDataResponse;

import java.util.Date;

public interface OutboundTicketRepositoryCustom {
    PagingDataResponse findAllOutboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                             Integer pageSize, Integer pageNumber);
}
