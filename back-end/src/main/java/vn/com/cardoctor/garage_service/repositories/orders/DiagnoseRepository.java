package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.entities.orders.Diagnose;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnoseRepository extends CrudRepository<Diagnose, Long> {
    Optional<Diagnose> findById(Long id);

    Optional<Diagnose> findByRepairOrderId(Long id);
}
