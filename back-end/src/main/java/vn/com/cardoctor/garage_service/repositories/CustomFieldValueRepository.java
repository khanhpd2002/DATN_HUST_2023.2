package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.CustomFieldValue;

public interface CustomFieldValueRepository extends CrudRepository<CustomFieldValue, Long> {
    @Modifying
    @Query(value = "delete from custom_field_values cfv where cfv.custom_field_id in ( select cf.id from custom_fields cf where cf.model_id = :modelId )", nativeQuery = true)
    void deleteByModelId(Long modelId);
}
