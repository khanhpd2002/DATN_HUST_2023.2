package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.Distributor;

import java.util.Optional;

@Repository
public interface DistributorRepository extends CrudRepository<Distributor, Long>, DistributorRepositoryCustom {
    Optional<Distributor> findByCodeAndGarageId(String code, Long garageId);
}
