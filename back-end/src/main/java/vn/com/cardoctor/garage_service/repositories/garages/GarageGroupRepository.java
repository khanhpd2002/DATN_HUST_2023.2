package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import vn.com.cardoctor.garage_service.entities.garages.GarageGroup;

import java.util.List;

public interface GarageGroupRepository extends CrudRepository<GarageGroup, Long>, GarageGroupRepositoryCustom {
//    List<GarageGroup> findAllBy

    @Query(value = "select * from garage_groups where id in :ids", nativeQuery = true)
    List<GarageGroup> findByListId(List<Long> ids);
}
