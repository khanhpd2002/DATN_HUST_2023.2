package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.ProductCompatibility;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductCompatibilityRepository extends CrudRepository<ProductCompatibility, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from product_compatibilities pc where pc.product_id = :productId", nativeQuery = true)
    void deleteAllByProductId(Long productId);

    List<ProductCompatibility> findAllByProductId(Long productId);
}
