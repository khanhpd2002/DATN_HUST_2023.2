package vn.com.cardoctor.garage_service.repositories;

import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.cardoctor.garage_service.entities.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryCustom {
    Optional<Customer> findById(Long id);

    Optional<Customer> findFirstByPhoneNumberAndGarageId(String phone, Long garageId);

    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
