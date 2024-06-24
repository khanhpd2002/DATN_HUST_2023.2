package vn.com.cardoctor.garage_service.services.orders;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.orders.Diagnose;
import vn.com.cardoctor.garage_service.entities.orders.RepairOrder;
import vn.com.cardoctor.garage_service.enums.RepairOrderStatus;
import vn.com.cardoctor.garage_service.models.requests.diagnose.DiagnoseRequest;
import vn.com.cardoctor.garage_service.repositories.orders.DiagnoseRepository;
import vn.com.cardoctor.garage_service.repositories.orders.RepairOrderRepository;

import java.util.*;

@Service
@Log4j2
public class DiagnoseService {
    private final DiagnoseRepository diagnoseRepository;
    private final RepairOrderRepository repairOrderRepository;

    public DiagnoseService(DiagnoseRepository diagnoseRepository, RepairOrderRepository repairOrderRepository) {
        this.diagnoseRepository = diagnoseRepository;
        this.repairOrderRepository = repairOrderRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createDiagnoseGms(Long repairOrderId, DiagnoseRequest diagnoseRequest, boolean isCreateDiagnose) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Optional<Diagnose> oDiagnose = diagnoseRepository.findByRepairOrderId(repairOrderId);
        if (oDiagnose.isPresent()) {
            if (oRepairOrder.get().getStatus().equals(RepairOrderStatus.QUOTATION_SEND.getCode())) {
                throw new ApiException(ERROR.BAD_REQUEST, "Phiếu sửa chữa đã tồn tại chẩn đoán");
            }
            Diagnose diagnose = oDiagnose.get();
            diagnose.setRepairOrderId(repairOrderId);
            diagnose.setEmployeeId(diagnoseRequest.getEmployeeId());
            diagnose.setDescription(diagnoseRequest.getDescription());
            String images = new Gson().toJson(diagnoseRequest.getImages());
            diagnose.setImages(images);
            this.diagnoseRepository.save(diagnose);
            if (isCreateDiagnose) {
                RepairOrder repairOrder = oRepairOrder.get();
                repairOrder.setStatus(RepairOrderStatus.QUOTATION_SEND.getCode());
                this.repairOrderRepository.save(repairOrder);
            }
            return diagnose.getId();
        }
        Diagnose diagnose = new Diagnose();
        diagnose.setRepairOrderId(repairOrderId);
        diagnose.setEmployeeId(diagnoseRequest.getEmployeeId());
        diagnose.setDescription(diagnoseRequest.getDescription());
        String images = new Gson().toJson(diagnoseRequest.getImages());
        diagnose.setImages(images);
        this.diagnoseRepository.save(diagnose);
        if (isCreateDiagnose) {
            RepairOrder repairOrder = oRepairOrder.get();
            repairOrder.setStatus(RepairOrderStatus.QUOTATION_SEND.getCode());
            this.repairOrderRepository.save(repairOrder);
        }
        return diagnose.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateDiagnoseGms(Long repairOrderId, DiagnoseRequest diagnoseRequest) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Optional<Diagnose> oDiagnose = diagnoseRepository.findByRepairOrderId(repairOrderId);
        if (oDiagnose.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Diagnose diagnose = oDiagnose.get();
        diagnose.setRepairOrderId(repairOrderId);
        diagnose.setEmployeeId(diagnoseRequest.getEmployeeId());
        diagnose.setDescription(diagnoseRequest.getDescription());
        String images = new Gson().toJson(diagnoseRequest.getImages());
        diagnose.setImages(images);
        this.diagnoseRepository.save(diagnose);
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setStatus(RepairOrderStatus.QUOTATION_SEND.getCode());
        return diagnose.getId();
    }
}
