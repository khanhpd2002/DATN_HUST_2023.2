package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.garages.GarageGarageOwner;
import vn.com.cardoctor.garage_service.entities.garages.GarageGarageOwnerId;

import java.util.List;

public interface GarageGarageOwnerRepository extends CrudRepository<GarageGarageOwner, GarageGarageOwnerId> {
    List<GarageGarageOwner> findAllByGarageOwnerId(Long garageOwnerId);

    @Transactional
    @Modifying
    @Query(value = "delete from garage_garage_owners ggo where ggo.garage_owner_id = :garageOwnerId ", nativeQuery = true)
    void deleteAllByGarageOwnerId(Long garageOwnerId);

    @Modifying
    @Query(value = "select ggo.garage_owner_id from garage_garage_owners ggo where ggo.garage_id = :garageId", nativeQuery = true)
    List<Long> findGarageOwnerIdByGarageId(Long garageId);
}
