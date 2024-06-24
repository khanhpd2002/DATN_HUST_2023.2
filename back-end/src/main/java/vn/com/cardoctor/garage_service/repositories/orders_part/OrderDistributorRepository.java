package vn.com.cardoctor.garage_service.repositories.orders_part;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.orders_part.OrderDistributor;

@Repository
public interface OrderDistributorRepository extends CrudRepository<OrderDistributor, Long>, OrderDistributorRepositoryCustom {
}
