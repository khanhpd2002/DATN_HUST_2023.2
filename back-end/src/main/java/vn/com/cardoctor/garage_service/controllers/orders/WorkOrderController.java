//package vn.com.cardoctor.garage_service.controllers.orders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import vn.com.cardoctor.garage_service.controllers.BaseController;
//import vn.com.cardoctor.garage_service.models.requests.work_order.WorkOrderRequest;
//import vn.com.cardoctor.garage_service.models.responses.work_order.WorkOrderResponse;
//import vn.com.cardoctor.garage_service.services.orders.WorkOrderService;
//import model.BaseResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import utils.ApiException;
//
//@RestController
//@CrossOrigin(allowedHeaders = "*", origins = "*")
//@RequestMapping("repair-orders/{repair-order-id}/work-orders")
//public class WorkOrderController extends BaseController {
//    @Autowired
//    WorkOrderService workOrderService;
//
//    @PostMapping
//    public BaseResponse createWorkOrder(@PathVariable("repair-order-id") Long repairOrderId,
//                                        @RequestBody WorkOrderRequest workOrderRequest) throws ApiException {
//        return workOrderService.createWorkOrder(repairOrderId, workOrderRequest);
//    }
//
//    @GetMapping("/{id}")
//    public WorkOrderResponse getDetailWorkOrder(@PathVariable("repair-order-id") Long repairOrderId, @PathVariable("id") Long id) throws ApiException, JsonProcessingException {
//        return workOrderService.getDetailWorkOrder(repairOrderId, id);
//    }
//
//    @PatchMapping("/{id}")
//    public BaseResponse updateWorkOrder(@PathVariable("repair-order-id") Long repairOrderId, @PathVariable("id") Long id,
//                                        @RequestBody WorkOrderRequest workOrderRequest) throws ApiException {
//        return workOrderService.updateWorkOrder(repairOrderId, id, workOrderRequest);
//    }
//}
