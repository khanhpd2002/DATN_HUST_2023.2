//package vn.com.cardoctor.garage_service.services.orders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import vn.com.cardoctor.garage_service.entities.orders.Quotation;
//import vn.com.cardoctor.garage_service.entities.orders.RepairOrder;
//import vn.com.cardoctor.garage_service.entities.orders.WorkOrder;
//import vn.com.cardoctor.garage_service.enums.QuotationStatus;
//import vn.com.cardoctor.garage_service.enums.RepairOrderStatus;
//import vn.com.cardoctor.garage_service.enums.WorkOrderStatus;
//import vn.com.cardoctor.garage_service.models.requests.work_order.WorkOrderRequest;
//import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
//import vn.com.cardoctor.garage_service.models.responses.work_order.WorkOrderResponse;
//import vn.com.cardoctor.garage_service.repositories.orders.QuotationRepository;
//import vn.com.cardoctor.garage_service.repositories.orders.RepairOrderRepository;
//import vn.com.cardoctor.garage_service.repositories.orders.WorkOrderRepository;
//import lombok.extern.log4j.Log4j2;
//import model.BaseResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import utils.ApiException;
//import utils.ERROR;
//import vn.com.cardoctor.garage_service.services.BaseService;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.Objects;
//import java.util.Optional;
//
//@Service
//@Log4j2
//public class WorkOrderService extends BaseService {
//    @Autowired
//    WorkOrderRepository workOrderRepository;
//
//    @Autowired
//    RepairOrderRepository repairOrderRepository;
//
//    @Autowired
//    QuotationRepository quotationRepository;
//
//    @Autowired
//    RepairOrderService repairOrderService;
//
//    public BaseResponse createWorkOrder(Long repairOrderId, WorkOrderRequest workOrderRequest) throws ApiException {
//        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
//        if (oRepairOrder.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        RepairOrder repairOrder = oRepairOrder.get();
//        Optional<Quotation> oQuotation = quotationRepository.findByRepairOrderId(repairOrder.getId());
//        Quotation quotation = oQuotation.get();
//        if (quotation.getStatus() != QuotationStatus.CONFIRM.getCode()) {
//            throw new ApiException(ERROR.BAD_REQUEST);
//        }
//        WorkOrder workOrder = new WorkOrder();
//
//        // Convert LocalTime to Date
//        if (workOrderRequest.getPlannedDuration() != null) {
//            LocalTime localTime = LocalTime.parse(workOrderRequest.getPlannedDuration().toString());
//            LocalDate localDate = LocalDate.now();
//            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//            Date plannedDuration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            workOrder.setPlannedDuration(plannedDuration);
//        } else {
//            workOrder.setPlannedDuration(null);
//        }
//        workOrder.setRepairOrderId(repairOrderId);
//        workOrder.setScheduledDate(workOrderRequest.getScheduledDate());
//        workOrder.setPlannedEndDate(workOrderRequest.getPlannedEndDate());
//        workOrder.setActualStartDate(workOrderRequest.getActualStartDate());
//        workOrder.setActualEndDate(workOrderRequest.getActualEndDate());
//        workOrder.setActualDuration(workOrderRequest.getActualDuration());
//        if (Objects.nonNull(workOrderRequest.getStatus())) {
//            workOrder.setStatus(workOrderRequest.getStatus());
//        } else {
//            workOrder.setStatus(WorkOrderStatus.IN_PROGRESS.getCode());
//        }
//        log.info("============== Work Order ============ {}", workOrder);
//        workOrderRepository.save(workOrder);
//        return new BaseResponse();
//    }
//
//    public BaseResponse updateWorkOrder(Long repairOrderId, Long id, WorkOrderRequest workOrderRequest) throws ApiException {
//        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
//        if (oRepairOrder.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        RepairOrder repairOrder = oRepairOrder.get();
//        Optional<Quotation> oQuotation = quotationRepository.findByRepairOrderId(repairOrder.getId());
//        Quotation quotation = oQuotation.get();
//        repairOrder.setStatus(RepairOrderStatus.WORK_IN_PROGRESS.getCode());
//        this.repairOrderRepository.save(repairOrder);
//        if (quotation.getStatus() != QuotationStatus.CONFIRM.getCode()) {
//            throw new ApiException(ERROR.BAD_REQUEST);
//        }
//        Optional<WorkOrder> oWorkOrder = workOrderRepository.findById(id);
//        if (oWorkOrder.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        WorkOrder workOrder = oWorkOrder.get();
//
//        // Convert LocalTime to Date
//        if (workOrderRequest.getPlannedDuration() != null) {
//            LocalTime localTime = LocalTime.parse(workOrderRequest.getPlannedDuration().toString());
//            LocalDate localDate = LocalDate.now();
//            LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
//            Date plannedDuration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            workOrder.setPlannedDuration(plannedDuration);
//        } else {
//            workOrder.setPlannedDuration(null);
//        }
//        workOrder.setRepairOrderId(repairOrderId);
//        workOrder.setScheduledDate(workOrderRequest.getScheduledDate());
//        workOrder.setPlannedEndDate(workOrderRequest.getPlannedEndDate());
//        workOrder.setActualStartDate(workOrderRequest.getActualStartDate());
//        workOrder.setActualEndDate(workOrderRequest.getActualEndDate());
//        workOrder.setActualDuration(workOrderRequest.getActualDuration());
//        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS.getCode());
//
//        workOrderRepository.save(workOrder);
//        return new BaseResponse();
//    }
//
////    public WorkOrderResponse getDetailWorkOrder(Long repairOrderId, Long workOrderId) throws ApiException, JsonProcessingException {
////        Optional<WorkOrder> oWorkOrder = workOrderRepository.findById(workOrderId);
////        if (oWorkOrder.isEmpty()) {
////            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
////        }
////        WorkOrder workOrder = oWorkOrder.get();
////        RepairOrderResponse repairOrderResponse = repairOrderService.getRepairOrder(workOrder.getRepairOrderId()).getData();
////        WorkOrderResponse workOrderResponse = new WorkOrderResponse();
////        workOrderResponse.setRepairOrderResponse(repairOrderResponse);
////        workOrderResponse.setScheduledDate(workOrder.getScheduledDate());
////        workOrderResponse.setActualEndDate(workOrder.getActualEndDate());
////        return workOrderResponse;
////    }
//}
