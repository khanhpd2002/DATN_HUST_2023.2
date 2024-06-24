package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.entities.CustomFieldOption;
import vn.com.cardoctor.garage_service.models.dtos.CustomFieldOptionDto;

import java.util.List;

public interface CustomFieldOptionRepositoryCustom {
    List<CustomFieldOption> getCustomFieldOption(String resourceName, Long modelId);
}
