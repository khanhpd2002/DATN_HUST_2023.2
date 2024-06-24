package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Query;
import vn.com.cardoctor.garage_service.entities.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends CrudRepository<Car, Long>, CarRepositoryCustom {
    Optional<Car> findById(Long id);

    Optional<Car> findByGarageIdAndDriverCarId(Long garageId, Long driverCarId);

    @Query(value = "select * from cars where id in :ids", nativeQuery = true)
    List<Car> findByListId(List<Long> ids);

    Optional<Car> findByVinNumberAndAndGarageId(String vinNumber, Long garageId);
}
