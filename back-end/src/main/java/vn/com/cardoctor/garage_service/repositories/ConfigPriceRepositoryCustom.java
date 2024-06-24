package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;
import vn.com.cardoctor.garage_service.models.dtos.ConfigPriceDto;

import java.util.List;

public interface ConfigPriceRepositoryCustom {
    PagingDataResponse findAllConfigPrice(Long garageId, Long type, String code, String name,
                                           Integer pageSize, Integer pageNumber);

    List<ConfigPriceDto> findConfigPriceByProductAndService(Long productId, Long garageServiceId);

    ConfigPriceDto findByCustomerTypeAndProductAndService(Long customerTypeId, Long modelId, Integer type);
}
