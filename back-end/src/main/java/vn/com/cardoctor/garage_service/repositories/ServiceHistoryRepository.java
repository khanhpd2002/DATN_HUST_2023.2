package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.entities.ServiceHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceHistoryRepository extends CrudRepository<ServiceHistory, Long> {
}
