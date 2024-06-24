package vn.com.cardoctor.garage_service.repositories.impls;

import vn.com.cardoctor.garage_service.entities.CustomFieldOption;
import vn.com.cardoctor.garage_service.models.dtos.CustomFieldDto;
import vn.com.cardoctor.garage_service.models.dtos.CustomFieldOptionDto;
import vn.com.cardoctor.garage_service.repositories.CustomFieldOptionRepositoryCustom;
import vn.com.cardoctor.garage_service.repositories.CustomFieldRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomFieldOptionRepositoryImpl implements CustomFieldOptionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CustomFieldOption> getCustomFieldOption(String resourceName, Long modelId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select cfo.* from custom_field_options cfo ");
        jpql.append("   join custom_fields cf on cfo.custom_field_id = cf.id ");
        jpql.append("   and cf.resource_name = :resourceName and cf.model_id = :modelId ");

        params.put("resourceName", resourceName);
        params.put("modelId", modelId);

        Query q = entityManager.createNativeQuery(jpql.toString(), CustomFieldOption.class);
        params.forEach(q::setParameter);

        return q.getResultList();
    }
}
