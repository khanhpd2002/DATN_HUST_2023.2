package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.OutboundTicket;

@Repository
public interface OutboundTicketRepository extends JpaRepository<OutboundTicket, Long>, JpaSpecificationExecutor<OutboundTicket>, OutboundTicketRepositoryCustom {
    Integer countOutboundTicketByTicketIdAndType(Long ticketId, Integer type);
}
