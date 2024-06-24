package vn.com.cardoctor.garage_service.repositories.garages;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.entities.garages.GarageContract;

public interface GarageContractRepositoryCustom {
    PagingDataResponse findAllGarageContract(String garageName, Integer pageSize, Integer pageNumber);

}
