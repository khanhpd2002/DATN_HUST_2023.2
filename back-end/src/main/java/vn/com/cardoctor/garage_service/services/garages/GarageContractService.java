package vn.com.cardoctor.garage_service.services.garages;

import model.BaseResponse;
import model.PagingDataResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageContract;
import vn.com.cardoctor.garage_service.enums.EntityStatus;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageContractRequest;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageContractResponse;
import vn.com.cardoctor.garage_service.repositories.garages.GarageContractRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;

import java.util.Optional;

@Service
public class GarageContractService {
    @Autowired
    GarageContractRepository garageContractRepository;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    ModelMapper modelMapper;

    public PagingDataResponse findAllGarageContract(String garageName, Integer pageSize, Integer pageNumber) {
        PagingDataResponse response = this.garageContractRepository.findAllGarageContract(garageName, pageSize, pageNumber);
        return response;
    }

    public BaseResponse<GarageContractResponse> findById(Long id) throws ApiException {
        GarageContractResponse garageContractResponse = new GarageContractResponse();
        Optional<GarageContract> oGarageContract = this.garageContractRepository.findById(id);
        if (oGarageContract.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại hợp đồng này!");
        }
        GarageContract garageContract = oGarageContract.get();
        garageContractResponse = this.modelMapper.map(garageContract, GarageContractResponse.class);
        Long garageId = garageContract.getGarageId();
        if (garageId != null) {
            Garage garage = this.garageRepository.findById(id).get();
            garageContractResponse.setGarage(garage);
        }
        return new BaseResponse(1, "Success", garageContractResponse);
    }

    public BaseResponse create(GarageContractRequest garageContractRequest) {
        GarageContract garageContract = new GarageContract();
        this.convertRequestToEntity(garageContract, garageContractRequest);
        garageContract.setStatus(EntityStatus.ACCEPTED.getCode());
        this.garageContractRepository.save(garageContract);
        return new BaseResponse<>();
    }

    public BaseResponse update(GarageContractRequest garageContractRequest, Long id) throws ApiException {
        Optional<GarageContract> oGarageContract = this.garageContractRepository.findById(id);
        if (oGarageContract.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại hợp đồng này!");
        }
        GarageContract garageContract = oGarageContract.get();
        this.convertRequestToEntity(garageContract, garageContractRequest);
        this.garageContractRepository.save(garageContract);
        return new BaseResponse<>();
    }

    public void convertRequestToEntity(GarageContract garageContract, GarageContractRequest garageContractRequest) {
        garageContract.setContractNumber(garageContractRequest.getContractNumber());
        garageContract.setGarageId(garageContractRequest.getGarageId());
        garageContract.setContractFromDate(garageContractRequest.getContractFromDate());
        garageContract.setContractToDate(garageContractRequest.getContractToDate());
        garageContract.setDocument(garageContractRequest.getDocument());
    }
}
