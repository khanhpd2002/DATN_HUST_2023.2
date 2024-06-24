package vn.com.cardoctor.garage_service.services.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Tag;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.tag.TagRequest;
import vn.com.cardoctor.garage_service.repositories.garages.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;

    public PagingDataResponse findAllTag(String name, String description, Integer status,
                                         Integer pageSize, Integer pageNumber) throws ApiException {
        return this.tagRepository.findAllTag(name, description, status, pageSize, pageNumber);
    }

    public BaseResponse<Tag> findById(Long id) throws ApiException {
        Optional<Tag> oTag = this.tagRepository.findById(id);
        if (oTag.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        return new BaseResponse<>(1, "Success", oTag.get());
    }
    public BaseResponse create(TagRequest tagRequest) throws ApiException {
        if (tagRequest.getName() == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Invalid Request");
        }
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        tag.setDescription(tagRequest.getDescription());
        tag.setStatus(EntityStatus.PENDING.getCode());
        this.tagRepository.save(tag);
        return new BaseResponse(1, "Create tag success");
    }

    public BaseResponse update(TagRequest tagRequest, Long id) throws ApiException {
        Optional<Tag> oTag = this.tagRepository.findById(id);
        if (oTag.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        Tag tag = oTag.get();
        tag.setName(tagRequest.getName());
        tag.setDescription(tagRequest.getDescription());
        this.tagRepository.save(tag);
        return new BaseResponse(1, "Update tag success");
    }

    public BaseResponse accept(Long id) throws ApiException {
        Optional<Tag> oTag = this.tagRepository.findById(id);
        if (oTag.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        Tag tag = oTag.get();
        tag.setStatus(EntityStatus.ACCEPTED.getCode());
        this.tagRepository.save(tag);
        return new BaseResponse(1, "Accept tag success");
    }

    public BaseResponse acceptList(List<Long> ids) throws ApiException {
        List<Tag> listTag = this.tagRepository.findByListId(ids);
        for (Tag tag : listTag) {
            tag.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.tagRepository.saveAll(listTag);
        return new BaseResponse(1, "Accept success");
    }

    public BaseResponse reject(Long id) throws ApiException {
        Optional<Tag> oTag = this.tagRepository.findById(id);
        if (oTag.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        Tag tag = oTag.get();
        tag.setStatus(EntityStatus.REJECTED.getCode());
        this.tagRepository.save(tag);
        return new BaseResponse(1, "Reject tag success");
    }

    public BaseResponse rejectList(List<Long> ids) throws ApiException {
        List<Tag> listTag = this.tagRepository.findByListId(ids);
        for (Tag tag : listTag) {
            tag.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.tagRepository.saveAll(listTag);
        return new BaseResponse(1, "Reject success");
    }
}
