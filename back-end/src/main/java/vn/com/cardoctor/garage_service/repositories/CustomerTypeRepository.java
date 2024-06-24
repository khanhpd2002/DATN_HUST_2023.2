package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.CustomerType;

import java.util.List;

public interface CustomerTypeRepository extends CrudRepository<CustomerType, Long>, CustomerTypeRepositoryCustom {
    List<CustomerType> findByGarageId(Long garageId);

    List<CustomerType> findByGarageIdIsNull();
}
