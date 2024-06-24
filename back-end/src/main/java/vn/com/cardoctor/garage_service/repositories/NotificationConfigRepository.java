package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.NotificationConfig;

import java.util.Optional;

@Repository
public interface NotificationConfigRepository extends CrudRepository<NotificationConfig, Long> {
    Optional<NotificationConfig> findByNotification(String notification);
}
