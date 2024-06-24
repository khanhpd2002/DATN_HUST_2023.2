package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.entities.orders.RepairOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderRepository extends CrudRepository<RepairOrder, Long>, RepairOrderRepositoryCustom {
//    Optional<RepairOrder> findById(Long id);

    @Query(value = "SELECT * FROM repair_orders limit :limit offset :offset", nativeQuery = true)
    List<RepairOrder> findPaging(Integer limit, Integer offset);
}
