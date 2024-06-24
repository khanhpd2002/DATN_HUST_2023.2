package vn.com.cardoctor.garage_service.repositories.garages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.garages.GarageServiceType;

import java.util.List;

@Repository
public interface GarageServiceTypeRepository extends JpaRepository<GarageServiceType, Long>, JpaSpecificationExecutor<GarageServiceType> {
    @Query(value = "select gst.* from garage_service_types gst where gst.garage_id = :garageId or gst.garage_id is null", nativeQuery = true)
    List<GarageServiceType> findByGarageId(Long garageId);

    @Query(value = "select gst.* from garage_service_types gst where gst.garage_id is null", nativeQuery = true)
    List<GarageServiceType> findServiceTypeApplyForAll();
}
