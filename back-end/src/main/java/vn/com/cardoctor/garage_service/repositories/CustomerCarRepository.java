package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.CustomerCar;
import vn.com.cardoctor.garage_service.entities.CustomerCarId;

import java.util.List;

public interface CustomerCarRepository extends CrudRepository<CustomerCar, CustomerCarId> {
    List<CustomerCar> findAllByCustomerId(Long customerId);

    @Modifying
    @Transactional
    @Query(value = "delete from customer_cars where customer_id = :customerId", nativeQuery = true)
    void deleteAllByCustomerId(Long customerId);
}
