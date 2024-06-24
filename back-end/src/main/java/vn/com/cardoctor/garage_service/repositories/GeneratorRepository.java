package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.Generator;

import java.util.Optional;

public interface GeneratorRepository extends CrudRepository<Generator, Long> {
    Optional<Generator> findByCode(String code);
}
