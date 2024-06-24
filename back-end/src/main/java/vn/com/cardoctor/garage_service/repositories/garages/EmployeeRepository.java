package vn.com.cardoctor.garage_service.repositories.garages;

import vn.com.cardoctor.garage_service.entities.garages.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, EmployeeRepositoryCustom {
    Optional<Employee> findById(Long id);

    Optional<Employee> findByFullName(String fullName);

    @Query(value = "DELETE FROM employees e WHERE e.id = :id", nativeQuery = true)
    void deteleById(Long id);

    @Query(value = "select * from employees where id in :ids", nativeQuery = true)
    List<Employee> findByListId(List<Long> ids);

    @Query(value = "select count(*) from employees where garage_id = :garageId and (status = 0 or status is null)", nativeQuery = true)
    BigDecimal getEmployeeCountByGarage(Long garageId);
}
