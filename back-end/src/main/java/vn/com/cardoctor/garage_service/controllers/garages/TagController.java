package vn.com.cardoctor.garage_service.controllers.garages;


import authentication.AcAuthentication;
import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.entities.garages.Tag;
import vn.com.cardoctor.garage_service.models.requests.tag.TagRequest;
import vn.com.cardoctor.garage_service.services.garages.TagService;

import java.util.List;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/tags")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping
    @AcAuthentication(enableAuthentication = true, applyClient = {"*"})
    public PagingDataResponse findAllTag(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false) Integer status,
                                                  @RequestParam Integer pageSize, @RequestParam Integer pageNumber) throws ApiException {
        return this.tagService.findAllTag(name, description, status, pageSize, pageNumber);
    }

    @GetMapping("/{id}")
    public BaseResponse<Tag> findById(@PathVariable(name = "id") Long id) throws ApiException {
        return this.tagService.findById(id);
    }

    @PostMapping
    public BaseResponse create(@RequestBody TagRequest tagRequest) throws ApiException {
        return this.tagService.create(tagRequest);
    }

    @PatchMapping("/{id}")
    public BaseResponse update(@RequestBody TagRequest tagRequest, @PathVariable(name = "id") Long id) throws ApiException {
        return this.tagService.update(tagRequest, id);
    }

    @PatchMapping("/{id}/accept")
    public BaseResponse accept(@PathVariable(name = "id") Long id) throws ApiException {
        return this.tagService.accept(id);
    }

    @PostMapping("/accept")
    public BaseResponse acceptList(@RequestBody List<Long> ids) throws ApiException {
        return this.tagService.acceptList(ids);
    }

    @PatchMapping("/{id}/reject")
    public BaseResponse reject(@PathVariable(name = "id") Long id) throws ApiException {
        return this.tagService.reject(id);
    }

    @PostMapping("/reject")
    public BaseResponse rejectList(@RequestBody List<Long> ids) throws ApiException {
        return this.tagService.rejectList(ids);
    }
}
