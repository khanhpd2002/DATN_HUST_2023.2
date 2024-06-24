package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.Rescue;

import java.util.List;

@Repository
public interface RescueRepository extends CrudRepository<Rescue, Long>, RescueRepositoryCustom {
    List<Rescue> findAll();

    @Query(value = "select * from rescues where id in :ids", nativeQuery = true)
    List<Rescue> findByListId(List<Long> ids);
}
