package vn.com.cardoctor.garage_service.repositories;

import vn.com.cardoctor.garage_service.models.dtos.CustomFieldDto;
import vn.com.cardoctor.garage_service.models.responses.custom_field.CustomFieldResponse;

import java.util.List;

public interface CustomFieldRepositoryCustom {
    List<CustomFieldDto> getCustomField(String resourceName, Long modelId);
}
