package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.com.cardoctor.garage_service.entities.ConfigPrice;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigPriceRepository extends CrudRepository<ConfigPrice, Long>, ConfigPriceRepositoryCustom {
    Optional<ConfigPrice> findByCustomerTypeIdAndAndProductId(Long customerTypeId, Long productId);

    Optional<ConfigPrice> findByCustomerTypeIdAndProductId(Long customerTypeId, Long productId);

    List<ConfigPrice> findAllByProductId(Long productId);

    List<ConfigPrice> findAllByGarageServiceId(Long garageServiceId);

    @Modifying
    @Transactional
    @Query(value = "delete from product_prices where id in :ids", nativeQuery = true)
    void deleteProductPrice(List<Long> ids);
}
