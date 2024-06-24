package vn.com.cardoctor.garage_service.repositories.impls;

import vn.com.cardoctor.garage_service.models.dtos.CustomFieldDto;
import vn.com.cardoctor.garage_service.models.responses.custom_field.CustomFieldResponse;
import vn.com.cardoctor.garage_service.repositories.CustomFieldRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomFieldRepositoryImpl implements CustomFieldRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CustomFieldDto> getCustomField(String resourceName, Long modelId) {
        StringBuilder jpql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        jpql.append("select cf.id, cf.field_type, cf.field_name, cf.label, cfv.option_value, cfv.text_value from custom_fields cf ");
        jpql.append("   left join custom_field_values cfv on cf.id = cfv.custom_field_id ");
        jpql.append("   where 1 = 1 ");

        if (Objects.nonNull(resourceName)) {
            params.put("resourceName", resourceName);
            jpql.append("and cf.resource_name = :resourceName ");
        }
        if (Objects.nonNull(modelId)) {
            params.put("modelId", modelId);
            jpql.append("and cf.model_id = :modelId ");
        }

        Query q = entityManager.createNativeQuery(jpql.toString(), CustomFieldDto.class);
        params.forEach(q::setParameter);

        return q.getResultList();
    }
}
