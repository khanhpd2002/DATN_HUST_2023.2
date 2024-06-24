package vn.com.cardoctor.garage_service.repositories.orders_part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.orders_part.LogOrderDistributor;
import vn.com.cardoctor.garage_service.entities.orders_part.LogSellSparePart;

import java.util.Optional;

@Repository
public interface LogOrderDistributorRepository extends JpaRepository<LogOrderDistributor, Long>, JpaSpecificationExecutor<LogOrderDistributor> {
    Optional<LogOrderDistributor> findFirstByOrderDistributorIdOrderByLogVersionDesc(Long orderDistributorId);
}

