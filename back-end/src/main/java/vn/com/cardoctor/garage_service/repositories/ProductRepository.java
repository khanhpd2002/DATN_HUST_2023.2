package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByCodeAndParentProductIdIsNullAndInventoryId(String code, Long inventoryId);

    Optional<Product> findByParentProductIdAndDistributorIdAndInventoryId(Long parentProductId, Long distributorId, Long inventoryId);

    Optional<Product> findByParentProductIdAndDistributorId(Long parentProductId, Long distributorId);

    List<Product> findAllByGarageId(Long garageId);

    Optional<Product> findByCode(String productCode);

    Optional<Product> findByCodeAndDistributorId(String productCode, Long distributorId);
}
