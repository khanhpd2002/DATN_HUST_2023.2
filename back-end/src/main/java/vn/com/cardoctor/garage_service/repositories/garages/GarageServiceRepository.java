package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageServiceRepository extends JpaRepository<GarageService, Long>, JpaSpecificationExecutor<GarageService>, GarageServiceRepositoryCustom {
    List<GarageService> findAllByGarageId(Long garageId);

    Optional<GarageService> findByNameAndGarageId(String name, Long garageId);
}
