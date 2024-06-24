package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.InboundTicket;

import java.util.Optional;

@Repository
public interface InboundTicketRepository extends JpaRepository<InboundTicket, Long>, JpaSpecificationExecutor<InboundTicket>, InboundTicketRepositoryCustom {
    Integer countInboundTicketByTicketIdAndType(Long ticketId, Integer type);
}
