package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.garages.GarageContract;

public interface GarageContractRepository extends CrudRepository<GarageContract, Long>, GarageContractRepositoryCustom {
}
