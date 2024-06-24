package vn.com.cardoctor.garage_service.repositories.orders;

import vn.com.cardoctor.garage_service.entities.orders.DiagnoseDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnoseDetailRepository extends CrudRepository<DiagnoseDetail, Long> {
    List<DiagnoseDetail> findAllByDiagnoseId(Long id);

    void deleteAllByDiagnoseId(Long diagnoseId);
}
