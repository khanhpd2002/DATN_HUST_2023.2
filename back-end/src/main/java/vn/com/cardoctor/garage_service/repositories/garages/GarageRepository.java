package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.Garage;

import java.util.List;

@Repository
public interface GarageRepository extends CrudRepository<Garage, Long>, GarageRepositoryCustom {
    List<Garage> findAll();

    @Query(value = "select * from garages where id in :ids", nativeQuery = true)
    List<Garage> findByListId(List<Long> ids);

    @Query(value = "select * from garages where id >= :id", nativeQuery = true)
    List<Garage> findAllGarageFromId(Long id);
}
