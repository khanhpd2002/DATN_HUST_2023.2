package vn.com.cardoctor.garage_service.repositories.orders;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.garages.ServiceCarSubSystem;
import vn.com.cardoctor.garage_service.entities.garages.ServiceCarSubSystemId;

import java.util.List;

@Repository
public interface ServiceCarSubSystemRepository extends CrudRepository<ServiceCarSubSystem, ServiceCarSubSystemId> {

    @Transactional
    @Modifying
    void deleteAllByServiceId(Long serviceId);

    List<ServiceCarSubSystem> findAllByServiceId(Long serviceId);
}
