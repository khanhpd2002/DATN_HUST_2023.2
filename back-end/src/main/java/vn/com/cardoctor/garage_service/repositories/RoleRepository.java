package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.entities.Role;
import vn.com.cardoctor.garage_service.enums.ERole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
