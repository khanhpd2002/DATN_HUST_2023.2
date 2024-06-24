package vn.com.cardoctor.garage_service.repositories;

import model.PagingDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.cardoctor.garage_service.entities.dashboards.LongTermInventoryProduct;
import vn.com.cardoctor.garage_service.models.dtos.ProductDto;
import vn.com.cardoctor.garage_service.models.dtos.ProductHistoryDto;
import vn.com.cardoctor.garage_service.models.responses.dashboards.LowInventoryProduct;
import vn.com.cardoctor.garage_service.models.responses.dashboards.OutputOrderDistributor;

import java.util.List;

public interface ProductRepositoryCustom {
    PagingDataResponse findAllProduct(Integer sparePartType, String code, String name, Long carBrandId, Long carModelId, Long carYearId, Long carVersionId,
                                      Long distributorId, Long inventoryId, Long garageId, Integer pageSize, Integer pageNumber);

    PagingDataResponse findAllParentProduct(Long inventoryId, Long distributorId, Integer sparePartType, String code, String name, Integer pageSize, Integer pageNumber);

    List<ProductHistoryDto> getAveragePriceProduct(Long inventoryId);

    ProductDto findProductById(Long productId);

    List<LowInventoryProduct> findAllLowInventoryProducts(Long garageId, Long quantityLabel);

    Page<LongTermInventoryProduct> findAllLongTermInventoryProducts(Long garageId, Pageable pageable);
}
