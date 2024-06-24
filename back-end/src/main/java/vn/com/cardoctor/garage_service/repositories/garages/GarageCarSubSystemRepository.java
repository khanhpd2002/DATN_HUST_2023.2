package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.garages.GarageCarSubSystem;
import vn.com.cardoctor.garage_service.entities.garages.GarageCarSubSystemId;

import java.util.List;

public interface GarageCarSubSystemRepository extends CrudRepository<GarageCarSubSystem, GarageCarSubSystemId> {
    List<GarageCarSubSystem> findAllByGarageId(Long garageId);
    
    void deleteAllByGarageId(Long garageId);
}
