//package vn.com.cardoctor.garage_service.controllers.orders;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import vn.com.cardoctor.garage_service.controllers.BaseController;
//import vn.com.cardoctor.garage_service.models.requests.invoice.InvoiceRequest;
//import vn.com.cardoctor.garage_service.models.responses.invoice.InvoiceResponse;
//import vn.com.cardoctor.garage_service.services.orders.InvoiceService;
//import model.BaseResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import utils.ApiException;
//
//@RestController
//@CrossOrigin(allowedHeaders = "*", origins = "*")
//@RequestMapping("repair-orders/{repair-order-id}/invoices")
//public class InvoiceController extends BaseController {
//    @Autowired
//    InvoiceService invoiceService;
//
//    @PostMapping
//    public BaseResponse createInvoice(@PathVariable("repair-order-id") Long repairOrderId,
//                                      @RequestBody InvoiceRequest invoiceRequest) throws ApiException {
//        return invoiceService.createInvoice(repairOrderId, invoiceRequest);
//    }
//
//    @GetMapping("/{id}")
//    public InvoiceResponse getDetailInvoice(@PathVariable("repair-order-id") Long repairOrderId, @PathVariable("id") Long id) throws ApiException, JsonProcessingException {
//        return invoiceService.getDetailInvoice(repairOrderId, id);
//    }
//}
