package vn.com.cardoctor.garage_service.repositories.in_out_bounds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.InboundProduct;

import java.util.List;

@Repository
public interface InboundProductRepository extends JpaRepository<InboundProduct, Long>, JpaSpecificationExecutor<InboundProduct>, InboundProductRepositoryCustom {
    List<InboundProduct> findByInboundTicketId(Long inboundTicketId);
}
