package vn.com.cardoctor.garage_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.repositories.orders.DiagnoseDetailRepository;
import vn.com.cardoctor.garage_service.repositories.orders.DiagnoseRepository;

@Service
public class SparePartService {
    @Autowired
    DiagnoseDetailRepository diagnoseDetailRepository;

    @Autowired
    DiagnoseRepository diagnoseRepository;

//    public void updateDiagnoseSpareParts(Diagnose diagnose, List<DiagnoseSparePart> diagnoseSpareParts) {
//        List<DiagnoseSparePart> currentDiagnoseSpareParts = diagnoseSparePartRepository.findAllByDiagnoseId(diagnose.getId());
//        diagnoseSparePartRepository.deleteAll(currentDiagnoseSpareParts);
//        diagnoseSpareParts.forEach(diagnoseSparePart -> {
//            diagnoseSparePart.setDiagnoseId(diagnose.getId());
//        });
//        diagnoseSparePartRepository.saveAll(diagnoseSpareParts);
//    }
}
