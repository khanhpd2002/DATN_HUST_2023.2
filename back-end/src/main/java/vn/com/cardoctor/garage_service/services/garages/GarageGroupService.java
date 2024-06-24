package vn.com.cardoctor.garage_service.services.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.GarageGroup;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageGroupRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageGroupResponse;
import vn.com.cardoctor.garage_service.repositories.garages.GarageGroupRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.services.BaseService;

import java.util.List;
import java.util.Optional;

@Service
public class GarageGroupService extends BaseService {
    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageGroupRepository garageGroupRepository;

    @Autowired
    ModelMapper modelMapper;

    public PagingDataResponse findGarageGroup(String name, String phone, String website, String email, String taxCode,
                                              Integer status, Integer pageSize, Integer pageNumber) {
        return this.garageGroupRepository.findGarageGroup(name, phone, website, email, taxCode, status, pageSize, pageNumber);
    }

    public GarageGroup findById(Long id) throws ApiException {
        Optional<GarageGroup> oGarageGroup = this.garageGroupRepository.findById(id);
        if (oGarageGroup.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Thông tin Gara tổng không tồn tại");
        }
        return oGarageGroup.get();
    }
    
    public Long createGarageGroup(GarageGroupRequest garageGroupRequest) throws ApiException {
        GarageGroup garageGroup = new GarageGroup();
        this.convertRequestToEntity(garageGroup, garageGroupRequest);
        garageGroup.setStatus(EntityStatus.PENDING.getCode());
        String code = String.format("GG%09d", Long.valueOf(this.getGeneratorValue("GG")));
        garageGroup.setCode(code);
        garageGroup = this.garageGroupRepository.save(garageGroup);
        return garageGroup.getId();
    }

    public Long updateGarageGroup(GarageGroupRequest garageGroupRequest, Long id) throws ApiException {
        Optional<GarageGroup> oGarageGroup = this.garageGroupRepository.findById(id);
        if (oGarageGroup.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại gara tổng!");
        }
        GarageGroup garageGroup = oGarageGroup.get();
        this.convertRequestToEntity(garageGroup, garageGroupRequest);
        this.garageGroupRepository.save(garageGroup);
        return id;
    }

    public String acceptList(List<Long> ids) {
        List<GarageGroup> garageGroups = this.garageGroupRepository.findByListId(ids);
        for (GarageGroup garageGroup : garageGroups) {
            garageGroup.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.garageGroupRepository.saveAll(garageGroups);
        return "Duyệt thành công";
    }

    public String rejectList(List<Long> ids) {
        List<GarageGroup> garageGroups = this.garageGroupRepository.findByListId(ids);
        for (GarageGroup garageGroup : garageGroups) {
            garageGroup.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.garageGroupRepository.saveAll(garageGroups);
        return "Từ chối thành công";
    }

    public void convertRequestToEntity(GarageGroup garageGroup, GarageGroupRequest garageGroupRequest) {
        garageGroup.setName(garageGroupRequest.getName());
        garageGroup.setPhone(garageGroupRequest.getPhone());
        garageGroup.setWebsite(garageGroupRequest.getWebsite());
        garageGroup.setEmail(garageGroupRequest.getEmail());
        garageGroup.setTaxCode(garageGroupRequest.getTaxCode());
        garageGroup.setDescription(garageGroupRequest.getDescription());
        garageGroup.setInsurancePartner(garageGroupRequest.getInsurancePartner());
        garageGroup.setManagementExp(garageGroupRequest.getManagementExp());
        garageGroup.setPlaceOfIssue(garageGroupRequest.getPlaceOfIssue());
        garageGroup.setDateOfIssue(garageGroupRequest.getDateOfIssue());
        garageGroup.setAddress(garageGroupRequest.getAddress());
        garageGroup.setType(garageGroupRequest.getType());
    }
}
