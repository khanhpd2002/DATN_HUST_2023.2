package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.entities.orders.WorkOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends CrudRepository<WorkOrder, Long> {
}
