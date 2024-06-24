package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.ProductLog;

import java.util.Optional;

@Repository
public interface ProductLogRepository extends JpaRepository<ProductLog, Long>, JpaSpecificationExecutor<ProductLog> {
    Optional<ProductLog> findFirstByProductIdOrderByCreatedAtDesc(Long productId);
}
