package vn.com.cardoctor.garage_service.services;

import model.BaseResponse;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.Distributor;
import vn.com.cardoctor.garage_service.models.requests.distributor.DistributorRequest;
import vn.com.cardoctor.garage_service.repositories.DistributorRepository;

import java.util.Optional;

@Service
public class DistributorService {
    @Autowired
    DistributorRepository distributorRepository;

    public PagingDataResponse findAllDistributor(String code, String name, Long provinceId, Long districtId, Long wardId,
                                                 String address, Long garageId, Integer pageSize, Integer pageNumber) {
        return this.distributorRepository.findAllDistributor(code, name, provinceId, districtId, wardId, address, garageId, pageSize, pageNumber);
    }

    public BaseResponse<Distributor> findById(Long id) throws ApiException {
        Optional<Distributor> oDistributor = this.distributorRepository.findById(id);
        if (oDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin nhà phân phối này!");
        }
        Distributor distributor = oDistributor.get();
        return new BaseResponse<>(1, "Success", distributor);
    }

    public BaseResponse create(DistributorRequest distributorRequest, Long garageId) throws ApiException {
        this.validRequest(distributorRequest, garageId, 0l);
        Distributor distributor = new Distributor();
        this.convertRequestToEntity(distributor, distributorRequest);
        distributor.setGarageId(garageId);
        distributor.setIsDelete(0);
        this.distributorRepository.save(distributor);
        return new BaseResponse<>();
    }

    public BaseResponse update(DistributorRequest distributorRequest, Long garageId, Long distributorId) throws ApiException {
        this.validRequest(distributorRequest, garageId, distributorId);
        Optional<Distributor> oDistributor = this.distributorRepository.findById(distributorId);
        if (oDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin nhà phân phối này!");
        }
        Distributor distributor = oDistributor.get();
        this.convertRequestToEntity(distributor, distributorRequest);
        distributor.setGarageId(garageId);
        this.distributorRepository.save(distributor);
        return new BaseResponse<>();
    }

    public BaseResponse delete(Long garageId, Long distributorId) throws ApiException {
        Optional<Distributor> oDistributor = this.distributorRepository.findById(distributorId);
        if (oDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin nhà phân phối này!");
        }
        Distributor distributor = oDistributor.get();
        distributor.setIsDelete(1);
        this.distributorRepository.save(distributor);
        return new BaseResponse();
    }

    public void convertRequestToEntity(Distributor distributor, DistributorRequest distributorRequest) {
        distributor.setCode(distributorRequest.getCode());
        distributor.setName(distributorRequest.getName());
        distributor.setProvinceId(distributorRequest.getProvinceId());
        distributor.setDistrictId(distributorRequest.getDistrictId());
        distributor.setWardId(distributorRequest.getWardId());
        distributor.setAddress(distributorRequest.getAddress());
        distributor.setContactName(distributorRequest.getContactName());
        distributor.setContactPhone(distributorRequest.getContactPhone());
    }

    public void validRequest(DistributorRequest distributorRequest, Long garageId, Long distributorId) throws ApiException {
        if (distributorRequest.getCode() == null || distributorRequest.getCode() == "") {
            throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin mã nhà phân phối");
        }
        Optional<Distributor> oDistributor = this.distributorRepository.findByCodeAndGarageId(distributorRequest.getCode(), garageId);
        if (oDistributor.isPresent() && !(oDistributor.get().getId() == distributorId.longValue())) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Thông tin nhà phân phối đã tồn tại");
        }
    }
}
