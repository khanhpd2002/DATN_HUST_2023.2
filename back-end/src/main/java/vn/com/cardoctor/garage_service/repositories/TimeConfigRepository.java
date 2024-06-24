package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.TimeConfig;

import java.util.Optional;

@Repository
public interface TimeConfigRepository extends CrudRepository<TimeConfig, Long> {
    Optional<TimeConfig> findByTimeFrame(Integer timeFrame);
}

