package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.CustomFieldOption;
import vn.com.cardoctor.garage_service.repositories.CustomFieldOptionRepositoryCustom;

@Repository
public interface CustomFieldOptionRepository extends CrudRepository<CustomFieldOption, Long>, CustomFieldOptionRepositoryCustom {
}
