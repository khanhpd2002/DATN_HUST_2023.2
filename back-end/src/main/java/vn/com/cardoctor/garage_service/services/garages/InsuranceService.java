package vn.com.cardoctor.garage_service.services.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Insurance;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.insurance.InsuranceRequest;
import vn.com.cardoctor.garage_service.repositories.garages.InsuranceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceService {
    @Autowired
    InsuranceRepository insuranceRepository;

    public PagingDataResponse findAllInsurance(String name, String description, Integer status,
                                         Integer pageSize, Integer pageNumber) {
        return this.insuranceRepository.findAllInsurance(name, description, status, pageSize, pageNumber);
    }

    public BaseResponse<Insurance> findById(Long id) throws ApiException {
        Optional<Insurance> oInsurance = this.insuranceRepository.findById(id);
        if (oInsurance.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin bảo hiểm");
        }
        return new BaseResponse<>(1, "Success", oInsurance.get());
    }

    public BaseResponse create(InsuranceRequest insuranceRequest) {
        Insurance insurance = new Insurance();
        insurance.setName(insuranceRequest.getName());
        insurance.setDescription(insuranceRequest.getDescription());
        insurance.setStatus(EntityStatus.PENDING.getCode());
        this.insuranceRepository.save(insurance);
        return new BaseResponse();
    }

    public BaseResponse update(InsuranceRequest insuranceRequest, Long id) throws ApiException {
        Optional<Insurance> oInsurance = this.insuranceRepository.findById(id);
        if (oInsurance.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin bảo hiểm");
        }
        Insurance insurance = oInsurance.get();
        insurance.setName(insuranceRequest.getName());
        insurance.setDescription(insuranceRequest.getDescription());
        this.insuranceRepository.save(insurance);
        return new BaseResponse();
    }

    public BaseResponse acceptList(List<Long> ids) throws ApiException {
        List<Insurance> listInsurance = this.insuranceRepository.findByListId(ids);
        for (Insurance insurance : listInsurance) {
            insurance.setStatus(EntityStatus.ACCEPTED.getCode());
        }
        this.insuranceRepository.saveAll(listInsurance);
        return new BaseResponse(1, "Accept success");
    }

    public BaseResponse rejectList(List<Long> ids) throws ApiException {
        List<Insurance> listInsurance = this.insuranceRepository.findByListId(ids);
        for (Insurance insurance : listInsurance) {
            insurance.setStatus(EntityStatus.REJECTED.getCode());
        }
        this.insuranceRepository.saveAll(listInsurance);
        return new BaseResponse(1, "Reject success");
    }
}
