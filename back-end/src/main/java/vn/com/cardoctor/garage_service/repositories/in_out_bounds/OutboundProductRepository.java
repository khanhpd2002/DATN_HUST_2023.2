package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.OutboundProduct;

import java.util.List;

@Repository
public interface OutboundProductRepository extends JpaRepository<OutboundProduct, Long>, JpaSpecificationExecutor<OutboundProduct> {
    List<OutboundProduct> findByOutboundTicketId(Long outboundTicketId);
}
