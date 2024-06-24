package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.responses.dashboards.OutputOrderDistributor;

import java.util.List;

public interface DistributorRepositoryCustom {
    PagingDataResponse findAllDistributor(String code, String name, Long provinceId, Long districtId, Long wardId,
                                          String address, Long garageId, Integer pageSize, Integer pageNumber);
    List<OutputOrderDistributor> findAllOutputOrderDistributors(Long garageId, Long quantityLabel);
}
