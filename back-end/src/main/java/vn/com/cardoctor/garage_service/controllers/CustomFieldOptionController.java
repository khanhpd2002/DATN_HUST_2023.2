package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.CustomFieldOption;
import vn.com.cardoctor.garage_service.models.requests.custom_field.CreateOptionRequest;
import vn.com.cardoctor.garage_service.services.CustomFieldOptionService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("custom-field-options")
public class CustomFieldOptionController {
    @Autowired
    CustomFieldOptionService customFieldOptionService;

    @GetMapping
    public BaseResponse<List<CustomFieldOption>> getCustomFieldOption(@RequestParam String resourceName,
                                                                      @RequestParam Long modelId) throws ApiException {
        return this.customFieldOptionService.getCustomFieldOption(resourceName, modelId);
    }

    @PostMapping("/{resource-name}/{model-id}")
    public BaseResponse createCustomFieldOption(@PathVariable(name = "resource-name") String resourceName,
                                                @PathVariable(name = "model-id") Long modelId,
                                                @RequestBody List<CreateOptionRequest> createOptionRequests) throws ApiException {
        return this.customFieldOptionService.createCustomFieldOption(resourceName, modelId, createOptionRequests);
    }
}
