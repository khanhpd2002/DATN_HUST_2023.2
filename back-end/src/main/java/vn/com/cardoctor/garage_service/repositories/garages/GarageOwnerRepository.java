package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageOwnerRepository extends CrudRepository<GarageOwner, Long>, GarageOwnerRepositoryCustom {
    @Query(value = "select * from garage_owners where id in :ids", nativeQuery = true)
    List<GarageOwner> findByListId(List<Long> ids);

    Optional<GarageOwner> findByUserName(String username);

    Optional<GarageOwner> findByKeycloakId(String keycloakId);

    Optional<GarageOwner> findByPhone(String phone);
}
