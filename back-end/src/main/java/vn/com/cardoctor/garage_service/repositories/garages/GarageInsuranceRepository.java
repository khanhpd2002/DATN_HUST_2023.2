package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.GarageInsurance;
import vn.com.cardoctor.garage_service.entities.garages.GarageInsuranceId;

import java.util.List;

@Repository
public interface GarageInsuranceRepository extends CrudRepository<GarageInsurance, GarageInsuranceId> {
    List<GarageInsurance> findAllByGarageId(Long garageId);

    void deleteAllByGarageId(Long garageId);
}
