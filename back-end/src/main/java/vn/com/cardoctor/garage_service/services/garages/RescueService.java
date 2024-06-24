package vn.com.cardoctor.garage_service.services.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.rescue.RescueRequest;
import vn.com.cardoctor.garage_service.repositories.garages.RescueRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RescueService {
    @Autowired
    RescueRepository rescueRepository;

    public PagingDataResponse findAllRescue(String name, String description, Integer status,
                                            Integer pageSize, Integer pageNumber) throws ApiException {
        return this.rescueRepository.findAllRescue(name, description, status, pageSize, pageNumber);
    }

    public BaseResponse<Rescue> findById(Long id) throws ApiException {
        Optional<Rescue> oRescue = this.rescueRepository.findById(id);
        if (oRescue.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Tag is not exists");
        }
        return new BaseResponse<>(1, "Success", oRescue.get());
    }
    public BaseResponse create(RescueRequest rescueRequest) throws ApiException {
        if (rescueRequest.getName() == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Invalid Request");
        }
        Rescue rescue = new Rescue();
        rescue.setName(rescueRequest.getName());
        rescue.setDescription(rescueRequest.getDescription());
        rescue.setStatus(EntityStatus.PENDING.getCode());
        this.rescueRepository.save(rescue);
        return new BaseResponse(1, "Create rescue success");
    }

    public BaseResponse update(RescueRequest rescueRequest, Long id) throws ApiException {
        Optional<Rescue> oRescue = this.rescueRepository.findById(id);
        if (oRescue.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Rescue is not exists");
        }
        Rescue rescue = oRescue.get();
        rescue.setName(rescueRequest.getName());
        rescue.setDescription(rescueRequest.getDescription());
        this.rescueRepository.save(rescue);
        return new BaseResponse(1, "Update rescue success");
    }

    public BaseResponse accept(Long id) throws ApiException {
        Optional<Rescue> oRescue = this.rescueRepository.findById(id);
        if (oRescue.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Rescue is not exists");
        }
        Rescue rescue = oRescue.get();
        rescue.setStatus(EntityStatus.ACCEPTED.getCode());
        this.rescueRepository.save(rescue);
        return new BaseResponse(1, "Accept rescue success");
    }

    public BaseResponse acceptList(List<Long> ids) throws ApiException {
        List<Rescue> listRescue = this.rescueRepository.findByListId(ids);
        for (Rescue rescue : listRescue) {
            rescue.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.rescueRepository.saveAll(listRescue);
        return new BaseResponse(1, "Accept success");
    }

    public BaseResponse reject(Long id) throws ApiException {
        Optional<Rescue> oRescue = this.rescueRepository.findById(id);
        if (oRescue.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Rescue is not exists");
        }
        Rescue rescue = oRescue.get();
        rescue.setStatus(EntityStatus.ACCEPTED.getCode());
        this.rescueRepository.save(rescue);
        return new BaseResponse(1, "Reject rescue success");
    }

    public BaseResponse rejectList(List<Long> ids) throws ApiException {
        List<Rescue> listRescue = this.rescueRepository.findByListId(ids);
        for (Rescue rescue : listRescue) {
            rescue.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.rescueRepository.saveAll(listRescue);
        return new BaseResponse(1, "Reject success");
    }
}
