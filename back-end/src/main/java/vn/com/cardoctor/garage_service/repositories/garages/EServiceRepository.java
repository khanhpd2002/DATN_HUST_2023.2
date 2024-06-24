package vn.com.cardoctor.garage_service.repositories.garages;

import vn.com.cardoctor.garage_service.entities.garages.EService;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EServiceRepository extends CrudRepository<EService, Long>, EServiceRepositoryCustom {
    @Query(value = "DELETE FROM services WHERE id = :id", nativeQuery = true)
    void deteleById(Long id);

    @Query(value = "select * from services where id in :ids", nativeQuery = true)
    List<EService> findByListId(List<Long> ids);
}
