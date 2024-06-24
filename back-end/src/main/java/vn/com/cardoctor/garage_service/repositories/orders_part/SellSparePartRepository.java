package vn.com.cardoctor.garage_service.repositories.orders_part;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.orders_part.SellSparePart;

@Repository
public interface SellSparePartRepository extends CrudRepository<SellSparePart, Long>, SellSparePartRepositoryCustom {
}
