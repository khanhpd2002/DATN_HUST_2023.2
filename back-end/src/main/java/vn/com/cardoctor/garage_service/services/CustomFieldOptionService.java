package vn.com.cardoctor.garage_service.services;

import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.CustomField;
import vn.com.cardoctor.garage_service.entities.CustomFieldOption;
import vn.com.cardoctor.garage_service.models.requests.custom_field.CreateOptionRequest;
import vn.com.cardoctor.garage_service.models.requests.custom_field.CustomFieldOptionRequest;
import vn.com.cardoctor.garage_service.repositories.CustomFieldOptionRepository;
import vn.com.cardoctor.garage_service.repositories.CustomFieldRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class CustomFieldOptionService {
    @Autowired
    CustomFieldOptionRepository customFieldOptionRepository;

    @Autowired
    CustomFieldRepository customFieldRepository;

    public BaseResponse<List<CustomFieldOption>> getCustomFieldOption(String resourceName, Long modelId) throws ApiException {
        if (Objects.isNull(resourceName)) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Missing resource name");
        }
        List<CustomFieldOption> customFieldOptionDtos = this.customFieldOptionRepository.getCustomFieldOption(resourceName, modelId);
        return new BaseResponse<>(1, "Success", customFieldOptionDtos);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createCustomFieldOption(String resourceName, Long modelId,
                                                List<CreateOptionRequest> createOptionRequests) throws ApiException {
        List<CustomField> customFields = new ArrayList<>();
        for (CreateOptionRequest createOptionRequest : createOptionRequests) {
            CustomField customField = new CustomField();
            customField.setResourceName(resourceName);
            customField.setModelId(modelId);
            customField.setFieldType(createOptionRequest.getFieldType());
            customField.setFieldName(createOptionRequest.getFieldName());
            customField.setLabel(createOptionRequest.getLabel());
            customField.setVisible(createOptionRequest.getVisible());
            customField.setIsRequired(createOptionRequest.getIsRequired());
            customField = this.customFieldRepository.save(customField);
            if (Arrays.asList("Multiselect", "Checkbox", "Select", "Radio").contains(customField.getFieldType())) {
                List<CustomFieldOptionRequest> customFieldOptionRequests = createOptionRequest.getCustomFieldOptionRequests();
                List<CustomFieldOption> customFieldOptions = new ArrayList<>();
                for (CustomFieldOptionRequest customFieldOptionRequest : customFieldOptionRequests) {
                    CustomFieldOption customFieldOption = new CustomFieldOption();
                    customFieldOption.setCustomFieldId(customField.getId());
                    customFieldOption.setName(customFieldOptionRequest.getName());
                    customFieldOptions.add(customFieldOption);
                }
                this.customFieldOptionRepository.saveAll(customFieldOptions);
            }
        }
        return new BaseResponse(1, "Success");
    }
}
