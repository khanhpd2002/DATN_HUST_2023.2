package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.garages.ServiceTag;
import vn.com.cardoctor.garage_service.entities.garages.ServiceTagId;

import java.util.List;

@Repository
public interface ServiceTagRepository extends CrudRepository<ServiceTag, ServiceTagId> {
    @Transactional
    @Modifying
    void deleteAllByServiceId(Long serviceId);

    List<ServiceTag> findAllByServiceId(Long serviceId);
}
