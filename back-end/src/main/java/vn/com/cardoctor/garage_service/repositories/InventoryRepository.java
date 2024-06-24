package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.Inventory;

import java.util.Optional;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long>, InventoryRepositoryCustom {
    Optional<Inventory> findFirstByGarageId(Long garageId);
}
