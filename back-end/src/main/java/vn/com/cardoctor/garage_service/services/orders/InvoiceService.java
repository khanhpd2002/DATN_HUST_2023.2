//package vn.com.cardoctor.garage_service.services.orders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.google.gson.Gson;
//import model.BaseResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import utils.ApiException;
//import utils.ERROR;
//import vn.com.cardoctor.garage_service.entities.orders.Invoice;
//import vn.com.cardoctor.garage_service.entities.orders.RepairOrder;
//import vn.com.cardoctor.garage_service.enums.PaymentStatusEnum;
//import vn.com.cardoctor.garage_service.models.requests.invoice.InvoiceRequest;
//import vn.com.cardoctor.garage_service.models.responses.invoice.InvoiceResponse;
//import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
//import vn.com.cardoctor.garage_service.repositories.orders.InvoiceRepository;
//import vn.com.cardoctor.garage_service.repositories.orders.RepairOrderRepository;
//
//import java.util.Optional;
//
//@Service
//public class InvoiceService {
//    @Autowired
//    RepairOrderService repairOrderService;
//
//    @Autowired
//    InvoiceRepository invoiceRepository;
//
//    @Autowired
//    RepairOrderRepository repairOrderRepository;
//
//    public BaseResponse createInvoice(Long repairOrderId, InvoiceRequest invoiceRequest) throws ApiException {
//        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
//        if (oRepairOrder.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        RepairOrder repairOrder = oRepairOrder.get();
//        Invoice invoice = new Invoice();
//        invoice.setRepairOrderId(repairOrderId);
//        invoice.setPaymentType(invoiceRequest.getPaymentType());
//        invoice.setPaymentStatus(PaymentStatusEnum.NOT_PAY.getCode());
//        invoiceRepository.save(invoice);
//        return new BaseResponse();
//    }
//
//    public InvoiceResponse getDetailInvoice(Long repairOrderId, Long id) throws ApiException, JsonProcessingException {
//        Optional<Invoice> oInvoice = invoiceRepository.findById(id);
//        if (oInvoice.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        Invoice invoice = oInvoice.get();
//        InvoiceResponse invoiceResponse = new InvoiceResponse();
//        RepairOrderResponse repairOrderResponse = repairOrderService.getRepairOrder(repairOrderId).getData();
//
//        invoiceResponse.setRepairOrderResponse(repairOrderResponse);
//        invoiceResponse.setPaymentStatus(invoice.getPaymentStatus());
//        return invoiceResponse;
//    }
//
//    public BaseResponse updateInvoice(Long repairOrderId, InvoiceRequest invoiceRequest, Long invoiceId) throws ApiException {
//        Optional<Invoice> oInvoice = this.invoiceRepository.findById(invoiceId);
//        if (oInvoice.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin hoá đơn này");
//        }
//        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
//        if (oRepairOrder.isEmpty()) {
//            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
//        }
//        RepairOrder repairOrder = oRepairOrder.get();
//        Invoice invoice = oInvoice.get();
//        invoice.setRepairOrderId(repairOrderId);
//        invoice.setPaymentType(invoiceRequest.getPaymentType());
//        invoice.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
//        String images = new Gson().toJson(invoiceRequest.getImages());
//        invoice.setImages(images);
//        invoiceRepository.save(invoice);
//        return new BaseResponse();
//    }
//
//}
