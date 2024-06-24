package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.CarSubSystem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarSubSystemRepository extends CrudRepository<CarSubSystem, Long>, CarSubSystemRepositoryCustom {
    List<CarSubSystem> findAll();

    @Override
    Optional<CarSubSystem> findById(Long id);

    @Query(value = "select * from car_sub_systems where id in :ids", nativeQuery = true)
    List<CarSubSystem> findByListId(List<Long> ids);
}
