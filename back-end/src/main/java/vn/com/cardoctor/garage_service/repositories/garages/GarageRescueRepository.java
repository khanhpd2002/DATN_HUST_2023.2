package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.GarageRescue;
import vn.com.cardoctor.garage_service.entities.garages.GarageRescueId;

import java.util.List;

@Repository
public interface GarageRescueRepository extends CrudRepository<GarageRescue, GarageRescueId> {
    List<GarageRescue> findAllByGarageId(Long garageId);

    void deleteAllByGarageId(Long garageId);
}
