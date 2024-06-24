package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.Insurance;

import java.util.List;

@Repository
public interface InsuranceRepository extends CrudRepository<Insurance, Long>, InsuranceRepositoryCustom {
    @Query(value = "select * from insurances where id in :ids", nativeQuery = true)
    List<Insurance> findByListId(List<Long> ids);
}
