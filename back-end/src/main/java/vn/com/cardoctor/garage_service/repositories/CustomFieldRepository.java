package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.CustomField;

public interface CustomFieldRepository extends CrudRepository<CustomField, Long>, CustomFieldRepositoryCustom {
}
