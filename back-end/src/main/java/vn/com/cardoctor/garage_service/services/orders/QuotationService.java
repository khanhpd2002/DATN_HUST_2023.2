package vn.com.cardoctor.garage_service.services.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.BaseResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageService;
import vn.com.cardoctor.garage_service.entities.orders.*;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;
import vn.com.cardoctor.garage_service.models.requests.garage.GarageServiceRequest;
import vn.com.cardoctor.garage_service.models.requests.notification.SendNotificationRequest;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationGmsRequest;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationLabourRequest;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationSparePartGmsRequest;
import vn.com.cardoctor.garage_service.models.responses.garage.GarageServiceResponse;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationLabourResponse;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationSparePartResponse;
import vn.com.cardoctor.garage_service.models.responses.quotation.QuotationResponse;
import vn.com.cardoctor.garage_service.repositories.CarRepository;
import vn.com.cardoctor.garage_service.repositories.CustomerRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageGarageOwnerRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageServiceRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders.*;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.services.GeneratorCodeTicketService;
import vn.com.cardoctor.garage_service.services.NotificationService;
import vn.com.cardoctor.garage_service.services.ProductService;
import vn.com.cardoctor.garage_service.services.garages.GarageServiceService;
import vn.com.cardoctor.garage_service.utils.BuildNotificationUtil;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Service
public class QuotationService extends BaseService {

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private RepairOrderRepository repairOrderRepository;

    @Autowired
    private DiagnoseRepository diagnoseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    QuotationInfoRepository quotationInfoRepository;

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    GarageGarageOwnerRepository garageGarageOwnerRepository;

    @Autowired
    QuotationProductLogRepository quotationProductLogRepository;

    @Autowired
    OutboundTicketRepository outboundTicketRepository;

    @Autowired
    InboundTicketRepository inboundTicketRepository;

    @Autowired
    OutboundProductRepository outboundProductRepository;

    @Autowired
    InboundProductRepository inboundProductRepository;

    @Autowired
    GarageServiceRepository garageServiceRepository;

    @Autowired
    GarageServiceService garageServiceService;

    @Autowired
    GeneratorCodeTicketService generatorCodeTicketService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryRepository inventoryRepository;

    private static final Logger log = LogManager.getLogger(QuotationService.class);

    private static final String REPAIR_ORDER_NOT_EXISTS = "Yêu cầu sửa/chữa không tồn tại.";

    private static final String QUOTATION_NOT_EXISTS = "Báo giá không tồn tại.";

    @Transactional(rollbackFor = Exception.class)
    public Long createQuotationGms(Long repairOrderId, QuotationGmsRequest request, boolean isStartRepair, Integer discountType) throws ApiException, JsonProcessingException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, REPAIR_ORDER_NOT_EXISTS);
        }
        Optional<Diagnose> oDiagnose = diagnoseRepository.findByRepairOrderId(repairOrderId);
        if (oDiagnose.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Yêu cầu sửa/chữa chưa có chẩn đoán.");
        }
        Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(repairOrderId);
        Quotation quotation;
        if (oQuotation.isEmpty()) {
            quotation = new Quotation();
        } else {
            quotation = oQuotation.get();
            // Xoa bao gia chi tiet nhan cong
            this.quotationInfoRepository.deleteByQuotationId(quotation.getId());
        }
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setNote(request.getNote());
        quotation.setRepairOrderId(repairOrderId);
        quotation.setDiagnoseId(oDiagnose.get().getId());
        quotation.setType(QuotationEnum.BY_PRODUCT_SERVICE.getCode());
        quotation.setDiscountType(discountType);
        quotation = this.quotationRepository.save(quotation);
        Garage garage = null;
        Inventory inventory = null;

        // Set tổng tiền của báo giá và chi tiết dịch vụ nhân công + phụ tùng
        BigDecimal totalPrice = new BigDecimal(0);

        if (isStartRepair && request.getQuotationSpareParts().isEmpty() && request.getQuotationLabours().isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Vui lòng chọn công việc hoặc phụ tùng báo giá");
        }

        List<QuotationInfo> quotationInfos = new ArrayList<>();
        // Set báo giá chi tiết nhân công
        for (QuotationLabourRequest quotationLabour : request.getQuotationLabours()) {
            QuotationInfo quotationInfo = new QuotationInfo();
            if (quotationLabour.getGarageServiceId() != null) {
                quotationInfo.setGarageServiceId(quotationLabour.getGarageServiceId());
            } else {
                GarageServiceRequest garageServiceRequest = getGarageServiceRequest(quotationLabour);
                GarageServiceResponse garageServiceResponse = this.garageServiceService.create(repairOrder.getGarageId(), garageServiceRequest);
                quotationInfo.setGarageServiceId(garageServiceResponse.getGarageServiceId());
            }
            quotationInfo.setQuotationId(quotation.getId());
            quotationInfo.setType(QuotationTypeEnum.SERVICE.getCode());
            quotationInfo.setUnitPrice(quotationLabour.getUnitPrice());
            quotationInfo.setQuantity(quotationLabour.getQuantity() == null ? BigDecimal.ZERO : quotationLabour.getQuantity());
            quotationInfo.setDiscount(quotationLabour.getDiscount());
            quotationInfo.setTax(quotationLabour.getTax());
            BigDecimal originPrice = quotationLabour.getUnitPrice().multiply(quotationLabour.getQuantity());
            BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(quotationLabour.getDiscount()).multiply(new BigDecimal(1).add(quotationLabour.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(quotationLabour.getDiscount())).multiply(new BigDecimal(1).add(quotationLabour.getTax()));
            quotationInfo.setOriginPrice(originPrice);
            quotationInfo.setPrice(price);
            quotationInfo.setEmployeeId(quotationLabour.getEmployeeId());
            if (isStartRepair) {
                quotationInfo.setOutboundProductId(0L);
            }
            totalPrice = totalPrice.add(price);
            quotationInfos.add(quotationInfo);
        }

        // Tinh toan báo giá chi tiết vật tư, phụ tùng
        for (QuotationSparePartGmsRequest quotationSparePart : request.getQuotationSpareParts()) {
            BigDecimal originPrice = quotationSparePart.getUnitPrice().multiply(quotationSparePart.getQuantity());
            BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(quotationSparePart.getDiscount()).multiply(new BigDecimal(1).add(quotationSparePart.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(quotationSparePart.getDiscount())).multiply(new BigDecimal(1).add(quotationSparePart.getTax()));
            totalPrice = totalPrice.add(price);
        }

        // Lưu tạm thời báo giá sản phẩm vào log
        if (!isStartRepair) {
            QuotationProductLog quotationProductLog;
            Optional<QuotationProductLog> oQuotationProductLog = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(quotation.getId());
            if (oQuotationProductLog.isEmpty()) {
                quotationProductLog = new QuotationProductLog();
                quotationProductLog.setQuotationId(quotation.getId());
                quotationProductLog.setLogVersion(1);
                String content = new ObjectMapper().writeValueAsString(request.getQuotationSpareParts());
                quotationProductLog.setContent(content);
                log.info("content line 197 is empty is {}", content);
                this.quotationProductLogRepository.save(quotationProductLog);
            } else {
                quotationProductLog = oQuotationProductLog.get();
                QuotationProductLog newQuotationProductLog = new QuotationProductLog();
                newQuotationProductLog.setQuotationId(quotationProductLog.getQuotationId());
                newQuotationProductLog.setLogVersion(quotationProductLog.getLogVersion() + 1);
                String content = new ObjectMapper().writeValueAsString(request.getQuotationSpareParts());
                newQuotationProductLog.setContent(content);
                log.info("content line 206 is not empty is {}", content);
                this.quotationProductLogRepository.save(newQuotationProductLog);
            }
            repairOrder.setStatus(RepairOrderStatus.QUOTATION_SEND.getCode());
            quotation.setStatus(QuotationStatus.DRAFT.getCode());
            this.repairOrderRepository.save(repairOrder);
        }

        // Tao moi don xuat kho neu tien hanh sua chua
        if (isStartRepair) {
            // Nếu có phụ tùng -> tạo phiếu xuất kho
            if (!request.getQuotationSpareParts().isEmpty()) {
                repairOrder.setStatus(RepairOrderStatus.WORK_IN_PROGRESS.getCode());
                this.repairOrderRepository.save(repairOrder);
                OutboundTicket outboundTicket = new OutboundTicket();
                String code = repairOrder.getCode();
                String[] splitedCode = code.split("DV");
                String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_REPAIR_ORDER.getCode() + splitedCode[1];
                outboundCode = outboundCode + "-01";

                outboundTicket.setCode(outboundCode);
                outboundTicket.setType(OutboundType.FOR_REPAIR_ORDER.getCode());
                outboundTicket.setTicketId(repairOrderId);
                outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                outboundTicket.setGarageId(repairOrder.getGarageId());
                outboundTicket = this.outboundTicketRepository.save(outboundTicket);
                for (QuotationSparePartGmsRequest quotationSparePart : request.getQuotationSpareParts()) {
                    if (quotationSparePart.getProductId() == null) {
                        if (garage == null) {
                            garage = this.garageRepository.findById(repairOrder.getGarageId()).get();
                            inventory = this.inventoryRepository.findFirstByGarageId(garage.getId()).get();
                        }
                        ProductRequest product = new ProductRequest();
                        product.setName(quotationSparePart.getProductName());
                        product.setCode(garage.getCode() + "PT" + StringUtil.generateNumericString(6));
                        product.setUnit(quotationSparePart.getUnit());
                        quotationSparePart.setProductId(this.productService.create(product, inventory.getId()).getParentProductId());
                    }
                    OutboundProduct outboundProduct = new OutboundProduct();
                    outboundProduct.setOutboundTicketId(outboundTicket.getId());
                    outboundProduct.setProductId(quotationSparePart.getProductId());
                    outboundProduct.setUnit(quotationSparePart.getUnit());
                    outboundProduct.setRequestQuantity(quotationSparePart.getQuantity());
                    outboundProduct.setExportQuantity(BigDecimal.ZERO);
                    outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                    outboundProduct = this.outboundProductRepository.save(outboundProduct);
                    quotationSparePart.setOutboundProductId(outboundProduct.getId());
                    quotationSparePart.setOutboundTicketId(outboundTicket.getId());
                }

                for (QuotationSparePartGmsRequest sparePartGmsRequest : request.getQuotationSpareParts()) {
                    QuotationInfo quotationInfo = new QuotationInfo();
                    quotationInfo.setQuotationId(quotation.getId());
                    quotationInfo.setType(QuotationTypeEnum.SPARE_PART.getCode());
                    quotationInfo.setProductId(sparePartGmsRequest.getProductId());
                    quotationInfo.setUnitPrice(sparePartGmsRequest.getUnitPrice());
                    quotationInfo.setQuantity(sparePartGmsRequest.getQuantity() == null ? BigDecimal.ZERO : sparePartGmsRequest.getQuantity());
                    quotationInfo.setDiscount(sparePartGmsRequest.getDiscount());
                    quotationInfo.setTax(sparePartGmsRequest.getTax());
                    BigDecimal originPrice = sparePartGmsRequest.getUnitPrice().multiply(sparePartGmsRequest.getQuantity());
                    BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(sparePartGmsRequest.getDiscount()).multiply(new BigDecimal(1).add(sparePartGmsRequest.getTax()))
                            : originPrice.multiply(new BigDecimal(1).subtract(sparePartGmsRequest.getDiscount())).multiply(new BigDecimal(1).add(sparePartGmsRequest.getTax()));
                    sparePartGmsRequest.setOriginPrice(originPrice);
                    sparePartGmsRequest.setPrice(price);
                    quotationInfo.setOriginPrice(originPrice);
                    quotationInfo.setPrice(price);
                    quotationInfo.setStatus(sparePartGmsRequest.getStatus());
                    quotationInfo.setOutboundProductId(sparePartGmsRequest.getOutboundProductId());
                    quotationInfos.add(quotationInfo);
                }

                QuotationProductLog quotationProductLog;
                Optional<QuotationProductLog> oQuotationProductLog = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(quotation.getId());
                if (oQuotationProductLog.isEmpty()) {
                    quotationProductLog = new QuotationProductLog();
                    quotationProductLog.setQuotationId(quotation.getId());
                    quotationProductLog.setLogVersion(1);
                    String content = new ObjectMapper().writeValueAsString(request.getQuotationSpareParts());
                    quotationProductLog.setContent(content);
                    log.info("content line 272 is empty is {}", content);
                    this.quotationProductLogRepository.save(quotationProductLog);
                } else {
                    quotationProductLog = oQuotationProductLog.get();
                    QuotationProductLog newQuotationProductLog = new QuotationProductLog();
                    newQuotationProductLog.setQuotationId(quotationProductLog.getQuotationId());
                    newQuotationProductLog.setLogVersion(quotationProductLog.getLogVersion() + 1);
                    String content = new ObjectMapper().writeValueAsString(request.getQuotationSpareParts());
                    newQuotationProductLog.setContent(content);
                    log.info("content line 281 is not empty is {}", content);
                    this.quotationProductLogRepository.save(newQuotationProductLog);
                }
            } else {
                QuotationProductLog quotationProductLog = new QuotationProductLog();
                quotationProductLog.setQuotationId(quotation.getId());
                quotationProductLog.setLogVersion(1);
                String content = new ObjectMapper().writeValueAsString(Collections.emptyList());
                quotationProductLog.setContent(content);
                log.info("content line 290 is not empty is {}", content);
                this.quotationProductLogRepository.save(quotationProductLog);
            }
            repairOrder.setStatus(RepairOrderStatus.WORK_IN_PROGRESS.getCode());
            quotation.setStatus(QuotationStatus.SENT.getCode());
            this.repairOrderRepository.save(repairOrder);
        }
        this.quotationInfoRepository.saveAll(quotationInfos);
        quotation.setTotalPrice(totalPrice);
        this.quotationRepository.save(quotation);
        return quotation.getId();
    }

    private static GarageServiceRequest getGarageServiceRequest(QuotationLabourRequest quotationLabour) {
        String garageServiceName = quotationLabour.getGarageServiceName();
        GarageServiceRequest garageServiceRequest = new GarageServiceRequest();
        garageServiceRequest.setName(garageServiceName);
        garageServiceRequest.setGarageServiceTypeId(quotationLabour.getGarageServiceTypeId());
        garageServiceRequest.setIsActive(Boolean.TRUE);
        return garageServiceRequest;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateQuotationGms(Long repairOrderId, Long quotationId, QuotationGmsRequest request, boolean isStartRepair) throws ApiException, JsonProcessingException {
        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, REPAIR_ORDER_NOT_EXISTS);
        }
        Optional<Quotation> oQuotation = quotationRepository.findById(quotationId);
        if (oQuotation.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, QUOTATION_NOT_EXISTS);
        }
        Quotation quotation = oQuotation.get();
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setNote(request.getNote());
        // Xoá và set lại giá trị chi tiết cho dịch vụ
        quotationInfoRepository.deleteByQuotationIdAndType(quotationId, QuotationTypeEnum.SERVICE.getCode());
        // Set tổng tiền của báo giá và chi tiết dịch vụ nhân công + phụ tùng
        BigDecimal totalPrice = new BigDecimal(0);
        List<QuotationInfo> quotationInfos = new ArrayList<>();
        Garage garage = null;
        Inventory inventory = null;

        if (isStartRepair && request.getQuotationSpareParts().isEmpty() && request.getQuotationLabours().isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Vui lòng chọn công việc hoặc phụ tùng báo giá");
        }

        // Set báo giá chi tiết nhân công
        for (QuotationLabourRequest quotationLabour : request.getQuotationLabours()) {
            QuotationInfo quotationInfo = new QuotationInfo();
            if (quotationLabour.getGarageServiceId() != null) {
                quotationInfo.setGarageServiceId(quotationLabour.getGarageServiceId());
            } else {
                String garageServiceName = quotationLabour.getGarageServiceName();
//                Optional<GarageService> oGarageService = this.garageServiceRepository.findByNameAndGarageId(garageServiceName, repairOrder.getGarageId());
//                if (oGarageService.isPresent()) {
//                    throw new ApiException(ERROR.INVALID_REQUEST, "Tên dịch vụ đã tồn tại");
//                }
                GarageServiceRequest garageServiceRequest = new GarageServiceRequest();
                garageServiceRequest.setName(garageServiceName);
                garageServiceRequest.setGarageServiceTypeId(quotationLabour.getGarageServiceTypeId());
                garageServiceRequest.setIsActive(Boolean.TRUE);
                GarageServiceResponse garageServiceResponse = this.garageServiceService.create(repairOrder.getGarageId(), garageServiceRequest);
                quotationInfo.setGarageServiceId(garageServiceResponse.getGarageServiceId());
            }
            quotationInfo.setQuotationId(quotation.getId());
            quotationInfo.setType(QuotationTypeEnum.SERVICE.getCode());
            quotationInfo.setUnitPrice(quotationLabour.getUnitPrice());
            quotationInfo.setQuantity(quotationLabour.getQuantity() == null ? BigDecimal.ZERO : quotationLabour.getQuantity());
            quotationInfo.setDiscount(quotationLabour.getDiscount());
            quotationInfo.setTax(quotationLabour.getTax());
            BigDecimal originPrice = quotationLabour.getUnitPrice().multiply(quotationLabour.getQuantity());
            BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(quotationLabour.getDiscount()).multiply(new BigDecimal(1).add(quotationLabour.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(quotationLabour.getDiscount())).multiply(new BigDecimal(1).add(quotationLabour.getTax()));
            quotationInfo.setOriginPrice(originPrice);
            quotationInfo.setPrice(price);
            quotationInfo.setEmployeeId(quotationLabour.getEmployeeId());
            if (isStartRepair) {
                quotationInfo.setOutboundProductId(0L);
            }
            totalPrice = totalPrice.add(price);
            quotationInfos.add(quotationInfo);
        }

        // Tinh toan báo giá chi tiết vật tư, phụ tùng
        for (QuotationSparePartGmsRequest quotationSparePart : request.getQuotationSpareParts()) {
            BigDecimal originPrice = quotationSparePart.getUnitPrice().multiply(quotationSparePart.getQuantity());
            BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(quotationSparePart.getDiscount()).multiply(new BigDecimal(1).add(quotationSparePart.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(quotationSparePart.getDiscount())).multiply(new BigDecimal(1).add(quotationSparePart.getTax()));
            totalPrice = totalPrice.add(price);
        }

        quotation.setTotalPrice(totalPrice);

        // Lấy thông tin phụ tùng hiện tại trong phiếu
        QuotationProductLog quotationProductLog = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(oQuotation.get().getId())
                .orElse(new QuotationProductLog());
        String content = quotationProductLog.getContent();
        System.out.println("Content is {} + content");
        log.info("Content is {}", content);
        List<QuotationSparePartGmsRequest> quotationSparePartInRepairOrder = new ObjectMapper().readValue(content, new TypeReference<List<QuotationSparePartGmsRequest>>(){});
        quotationSparePartInRepairOrder.removeIf(quotationSparePartGmsRequest -> quotationSparePartGmsRequest.getOutboundProductId().equals(0L));
        // Tao moi don xuat kho, nhập kho (nếu có) neu tien hanh sua chua
        if (isStartRepair) {
            // Nếu có thêm sản phẩm -> Tạo phiếu xuất kho
            if (!request.getAddQuotationSpareParts().isEmpty()) {
                OutboundTicket outboundTicket = new OutboundTicket();
                String code = repairOrder.getCode();
                String[] splitedCode = code.split("DV");
                String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_REPAIR_ORDER.getCode() + splitedCode[1];
                int numberOfOutbound = this.outboundTicketRepository.countOutboundTicketByTicketIdAndType(repairOrderId, OutboundType.FOR_REPAIR_ORDER.getCode()) + 1;
                outboundCode = outboundCode + "-" + String.format("%02d", numberOfOutbound);
                outboundTicket.setCode(outboundCode);
                outboundTicket.setType(OutboundType.FOR_REPAIR_ORDER.getCode());
                outboundTicket.setTicketId(repairOrderId);
                outboundTicket.setGarageId(repairOrder.getGarageId());
                outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                outboundTicket = this.outboundTicketRepository.save(outboundTicket);
                for (QuotationSparePartGmsRequest quotationSparePart : request.getAddQuotationSpareParts()) {
                    if (quotationSparePart.getProductId() == null) {
                        if (garage == null) {
                            garage = this.garageRepository.findById(repairOrder.getGarageId()).get();
                            inventory = this.inventoryRepository.findFirstByGarageId(garage.getId()).get();
                        }
                        ProductRequest product = new ProductRequest();
                        product.setName(quotationSparePart.getProductName());
                        product.setCode(garage.getCode() + "PT" + StringUtil.generateNumericString(6));
                        product.setUnit(quotationSparePart.getUnit());
                        quotationSparePart.setProductId(this.productService.create(product, inventory.getId()).getParentProductId());
                    }
                    OutboundProduct outboundProduct = new OutboundProduct();
                    outboundProduct.setOutboundTicketId(outboundTicket.getId());
                    outboundProduct.setProductId(quotationSparePart.getProductId());
                    outboundProduct.setUnit(quotationSparePart.getUnit());
                    outboundProduct.setRequestQuantity(quotationSparePart.getQuantity());
                    outboundProduct.setExportQuantity(BigDecimal.ZERO);
                    outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                    // Save thông tin sản phẩm xuất kho -> Thêm vào log của phụ tùng trong phiếu báo giá
                    outboundProduct = this.outboundProductRepository.save(outboundProduct);
                    quotationSparePart.setOutboundProductId(outboundProduct.getId());
                    quotationSparePart.setOutboundTicketId(outboundTicket.getId());
                    quotationSparePartInRepairOrder.add(quotationSparePart);
                    log.info("size is {} line 420 is {}", quotationSparePartInRepairOrder.size(), quotationSparePartInRepairOrder);

                    // Set lại báo giá phụ tùng
                }
            }

            // Xử lí nếu xoá sản phẩm
            // Nếu sản phẩm đã xuất kho -> Tạo phiếu nhập
            // Nếu sản phẩm chưa xuất kho -> Ẩn phiếu xuất kho chứa sản phẩm đó, tạo phiếu xuất kho mới với các sản phẩm còn lại
            if (!request.getRemoveQuotationSpareParts().isEmpty()) {
                InboundTicket inboundTicketForOutbounded; // Phiếu nhập kho cho sản phẩm đã xuất kho
                List<InboundProduct> inboundProductsForOutbounded = new ArrayList<>(); // List sản phẩm thuộc phiếu nhập kho trên

                OutboundTicket outboundTicketForNotOutbounded = new OutboundTicket(); // Phiếu xuất kho cho sản phẩm còn lại chưa xuất kho
                List<OutboundProduct> outboundProductsForNotOutbounded = new ArrayList<>(); // List sản phẩm thuộc phiếu xuất kho trên
                Set<Long> outboundTicketIdsDeleted = new HashSet<>(); // List id của các phiếu xuất kho chứa sản phẩm bị xoá gắn với phiếu dịch vụ
                List<OutboundProduct> outboundProductsInTicketDeleted = new ArrayList<>(); // List sản phẩm thuộc các phiếu trên

                for (QuotationSparePartGmsRequest quotationSparePart : request.getRemoveQuotationSpareParts()) {
                    // Nếu sản phẩm đã xuất kho -> Tạo phiếu nhập
                    if (quotationSparePart.getStatus().equals(OutboundProductStatus.DA_XUAT.getCode())) {
                        // Nếu sản phẩm đã xuất kho -> Thêm vào list inboundProductForOutbounded
                        InboundProduct inboundProduct = new InboundProduct();
                        inboundProduct.setProductId(quotationSparePart.getProductId());
                        inboundProduct.setUnit(quotationSparePart.getUnit());
                        inboundProduct.setDistributorId(0L);
                        inboundProduct.setRequestQuantity(quotationSparePart.getQuantity());
                        inboundProduct.setExportQuantity(BigDecimal.ZERO);
                        inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                        inboundProductsForOutbounded.add(inboundProduct);
                        // Xoá sản phẩm đó khỏi log của phụ tùng trong phiếu báo giá
                        quotationSparePartInRepairOrder.removeIf(sparePartInRepairOrder ->
                                sparePartInRepairOrder.getOutboundProductId().equals(quotationSparePart.getOutboundProductId())
                        );
                        log.info("size is {} line 469 is {}", quotationSparePartInRepairOrder.size(), quotationSparePartInRepairOrder);
                    }

                    // Nếu sản phẩm chưa xuất kho -> Huỷ phiếu xuất kho chứa sản phẩm đó
                    // Sau đó lưu các sản phẩm còn lại vào list inboundProductForNotOutbounded
                    // Tạo phiếu xuất kho mới với các sản phẩm trong list inboundProductForNotOutbounded
                    if (quotationSparePart.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                        // Đánh dấu phiếu xuất kho đã bị huỷ
                        Optional<OutboundProduct> oOutboundProductNotOutbounded = this.outboundProductRepository.findById(quotationSparePart.getOutboundProductId());
                        OutboundProduct outboundProductNotOutbounded = oOutboundProductNotOutbounded.get();
                        outboundTicketIdsDeleted.add(outboundProductNotOutbounded.getOutboundTicketId());
                    }
                }

                if (!inboundProductsForOutbounded.isEmpty()) {
                    inboundTicketForOutbounded = new InboundTicket();
                    String code = repairOrder.getCode();
                    String[] splitedCode = code.split("DV");
                    String inboundCode = splitedCode[0] + "NK" + InboundType.REFUND_REPAIR_ORDER.getCode() + splitedCode[1];
                    int numberOfInbound = this.inboundTicketRepository.countInboundTicketByTicketIdAndType(repairOrderId, InboundType.REFUND_REPAIR_ORDER.getCode()) + 1;
                    inboundCode = inboundCode + "-" + String.format("%02d", numberOfInbound);
                    inboundTicketForOutbounded.setCode(inboundCode);
                    inboundTicketForOutbounded.setType(InboundType.REFUND_REPAIR_ORDER.getCode());
                    inboundTicketForOutbounded.setTicketId(repairOrderId);
                    inboundTicketForOutbounded.setGarageId(repairOrder.getGarageId());
                    inboundTicketForOutbounded.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                    inboundTicketForOutbounded = this.inboundTicketRepository.save(inboundTicketForOutbounded);
                    for (InboundProduct inboundProduct : inboundProductsForOutbounded) {
                        inboundProduct.setInboundTicketId(inboundTicketForOutbounded.getId());
                    }
                    this.inboundProductRepository.saveAll(inboundProductsForOutbounded);
                }

                // Lưu thông tin vật tư ở các phiếu outboundTicketIdsDeleted
                // Set status cho phiếu đó thành "Đã xoá"
                // Xoá hết những sản phẩm ở phiếu dịch vụ ứng với phiếu outboundTicketIdsDeleted
                outboundTicketIdsDeleted.forEach(outboundTicketId -> {
                    Optional<OutboundTicket> oOutboundTicket = this.outboundTicketRepository.findById(outboundTicketId);
                    if (oOutboundTicket.isPresent()) {
                        OutboundTicket outboundTicket = oOutboundTicket.get();
                        outboundTicket.setStatus(InOutboundStatus.DELETED.getCode());
                        this.outboundTicketRepository.save(outboundTicket);
                    }
                    List<OutboundProduct> outboundProducts = this.outboundProductRepository.findByOutboundTicketId(outboundTicketId);
                    outboundProductsInTicketDeleted.addAll(outboundProducts);
//                    // Xoá hết những sản phẩm ở phiếu dịch vụ ứng với phiếu outboundTicketIdsDeleted
//                    for (OutboundProduct outboundProduct : outboundProducts) {
//                        quotationSparePartInRepairOrder.removeIf(sparePartInRepairOrder ->
//                                sparePartInRepairOrder.getOutboundProductId().equals(outboundProduct.getId())
//                        );
//                    }
                });

                // Remove những sản phẩm chưa xuất kho ở danh sách sản phẩm thuộc các phiếu outboundTicketIdsDeleted
                // Xoá những sản phẩm ở chưa xuất kho phiếu dịch vụ
                for (QuotationSparePartGmsRequest quotationSparePart : request.getRemoveQuotationSpareParts()) {
                    if (quotationSparePart.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                        outboundProductsInTicketDeleted.removeIf(product ->
                            product.getId().equals(quotationSparePart.getOutboundProductId())
                        );
                        quotationSparePartInRepairOrder.removeIf(sparePartInRepairOrder ->
                                sparePartInRepairOrder.getOutboundProductId().equals(quotationSparePart.getOutboundProductId())
                        );
                        log.info("size is {} line 536 is {}", quotationSparePartInRepairOrder.size(), quotationSparePartInRepairOrder);
                    }
                }

                // Tạo mới các sản phẩm thuộc phiếu xuất kho outboundTicketForNotOutbounded
                if (!outboundProductsInTicketDeleted.isEmpty()) {
                    String code = repairOrder.getCode();
                    String[] splitedCode = code.split("DV");
                    String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_REPAIR_ORDER.getCode() + splitedCode[1];
                    int numberOfOutbound = this.outboundTicketRepository.countOutboundTicketByTicketIdAndType(repairOrderId, OutboundType.FOR_REPAIR_ORDER.getCode()) + 1;
                    outboundCode = outboundCode + "-" + String.format("%02d", numberOfOutbound);

                    outboundTicketForNotOutbounded.setCode(outboundCode);
                    outboundTicketForNotOutbounded.setType(OutboundType.FOR_REPAIR_ORDER.getCode());
                    outboundTicketForNotOutbounded.setTicketId(repairOrderId);
                    outboundTicketForNotOutbounded.setGarageId(repairOrder.getGarageId());
                    outboundTicketForNotOutbounded.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                    outboundTicketForNotOutbounded = this.outboundTicketRepository.save(outboundTicketForNotOutbounded);

                    for (OutboundProduct outboundProductInTicketDeleted : outboundProductsInTicketDeleted) {
                        OutboundProduct outboundProduct = new OutboundProduct();
                        outboundProduct.setOutboundTicketId(outboundTicketForNotOutbounded.getId());
                        outboundProduct.setProductId(outboundProductInTicketDeleted.getProductId());
                        outboundProduct.setUnit(outboundProductInTicketDeleted.getUnit());
                        outboundProduct.setRequestQuantity(outboundProductInTicketDeleted.getRequestQuantity());
                        outboundProduct.setExportQuantity(BigDecimal.ZERO);
                        outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                        // Save thông tin sản phẩm xuất kho -> Sửa log của phụ tùng trong phiếu báo giá, trường outboundProductId
                        outboundProduct = this.outboundProductRepository.save(outboundProduct);
                        Optional<QuotationSparePartGmsRequest> oQuotationSparePartGmsRequest = quotationSparePartInRepairOrder
                                .stream()
                                .filter(sparePartInRepairOrder -> sparePartInRepairOrder.getOutboundProductId().equals(outboundProductInTicketDeleted.getId()))
                                .findAny();
                        if (oQuotationSparePartGmsRequest.isPresent()) {
                            QuotationSparePartGmsRequest quotationSparePartGmsRequest = oQuotationSparePartGmsRequest.get();
                            quotationSparePartGmsRequest.setOutboundProductId(outboundProduct.getId());
                            quotationSparePartGmsRequest.setOutboundTicketId(outboundTicketForNotOutbounded.getId());

                            // Set lại báo giá phụ tùng
                        }
                    }
                }

            }
            log.info(" line 589 is {}", quotationSparePartInRepairOrder);
            QuotationProductLog newQuotationProductLog = new QuotationProductLog();
            newQuotationProductLog.setQuotationId(quotationProductLog.getQuotationId());
            newQuotationProductLog.setLogVersion(quotationProductLog.getLogVersion() + 1);
            String newContent = new ObjectMapper().writeValueAsString(quotationSparePartInRepairOrder);
            newQuotationProductLog.setContent(newContent);
            this.quotationProductLogRepository.save(newQuotationProductLog);

            // Set lại thông tin dịch vụ nếu tiến hành sửa chữa
            quotationInfoRepository.deleteByQuotationIdAndType(quotationId, QuotationTypeEnum.SPARE_PART.getCode());
            repairOrder.setStatus(RepairOrderStatus.WORK_IN_PROGRESS.getCode());
            this.repairOrderRepository.save(repairOrder);
            for (QuotationSparePartGmsRequest sparePartGmsRequest : quotationSparePartInRepairOrder) {
                QuotationInfo quotationInfo = new QuotationInfo();
                quotationInfo.setQuotationId(quotation.getId());
                quotationInfo.setType(QuotationTypeEnum.SPARE_PART.getCode());
                quotationInfo.setProductId(sparePartGmsRequest.getProductId());
                quotationInfo.setUnitPrice(sparePartGmsRequest.getUnitPrice());
                quotationInfo.setQuantity(sparePartGmsRequest.getQuantity() == null ? BigDecimal.ZERO : sparePartGmsRequest.getQuantity());
                quotationInfo.setDiscount(sparePartGmsRequest.getDiscount());
                quotationInfo.setTax(sparePartGmsRequest.getTax());
                BigDecimal originPrice = sparePartGmsRequest.getUnitPrice().multiply(sparePartGmsRequest.getQuantity());
                BigDecimal price = quotation.getDiscountType().equals(1) ? originPrice.subtract(sparePartGmsRequest.getDiscount()).multiply(new BigDecimal(1).add(sparePartGmsRequest.getTax()))
                        : originPrice.multiply(new BigDecimal(1).subtract(sparePartGmsRequest.getDiscount())).multiply(new BigDecimal(1).add(sparePartGmsRequest.getTax()));
                quotationInfo.setOriginPrice(originPrice);
                quotationInfo.setPrice(price);
                quotationInfo.setStatus(sparePartGmsRequest.getStatus());
                quotationInfo.setOutboundProductId(sparePartGmsRequest.getOutboundProductId());
                quotationInfos.add(quotationInfo);
            }
        } else {
            QuotationProductLog newQuotationProductLog = new QuotationProductLog();
            newQuotationProductLog.setQuotationId(quotationProductLog.getQuotationId());
            newQuotationProductLog.setLogVersion(quotationProductLog.getLogVersion() + 1);
            String newContent = new ObjectMapper().writeValueAsString(request.getQuotationSpareParts());
            newQuotationProductLog.setContent(newContent != null ? newContent : "[]");
            newQuotationProductLog.setContent(newContent);
            this.quotationProductLogRepository.save(newQuotationProductLog);
        }
        quotationRepository.save(quotation);
        quotationInfoRepository.saveAll(quotationInfos);

        return quotationId;
    }

    @Transactional
    public Long changeQuotationStatus(Long repairOrderId, Long quotationId, Integer quotationStatus, HttpServletRequest httpServletRequest) throws ApiException {
        Optional<RepairOrder> optionalRepairOrder = repairOrderRepository.findById(repairOrderId);
        if (optionalRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, REPAIR_ORDER_NOT_EXISTS);
        }
        Optional<Quotation> optionalQuotation = quotationRepository.findById(quotationId);
        if (optionalQuotation.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, QUOTATION_NOT_EXISTS);
        }
        if (QuotationStatus.getQuotationStatus(quotationStatus) == null) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Trạng thái không chính xác.");
        }
        Optional<Car> oCar = this.carRepository.findById(optionalRepairOrder.get().getCarId());
        Car car = oCar.get();

        Quotation quotation = optionalQuotation.get();
        quotation.setStatus(quotationStatus);
        quotationRepository.save(quotation);

        if (quotationStatus.equals(QuotationStatus.CONFIRM.getCode())) {
            List<QuotationInfo> quotationInfos = this.quotationInfoRepository.findAllByQuotationIdAndType(quotationId, 1);
            List<Product> products = new ArrayList<>();
            for (QuotationInfo quotationInfo : quotationInfos) {
                Optional<Product> oProduct = this.productRepository.findById(quotationInfo.getProductId());
                if (oProduct.isEmpty()) {
                    throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Sản phẩm báo giá không có trong kho");
                }
                Product product = oProduct.get();
                product.setQuantity(product.getQuantity().subtract(quotationInfo.getQuantity()));
                products.add(product);
            }
            this.productRepository.saveAll(products);
            RepairOrder repairOrder = optionalRepairOrder.get();
            repairOrder.setStatus(RepairOrderStatus.WORK_IN_PROGRESS.getCode());
            repairOrderRepository.save(repairOrder);

            Map<String, String> params = new HashMap<>();
            NotificationConfig notification = this.notificationService.findByNotification("approve.quotation.garage");
            String title = String.format(notification.getTitle(), car.getLicensePlate());
            String body = String.format(notification.getBody(), car.getLicensePlate());
            String image = notification.getImage();
            List<Long> garageOwnerIds = this.garageGarageOwnerRepository.findGarageOwnerIdByGarageId(repairOrder.getGarageId());
            SendNotificationRequest sendNotificationRequest = BuildNotificationUtil
                    .buildSimpleNotificationRequest(httpServletRequest, params, title, body, image, garageOwnerIds);
            log.info("Request is {}", sendNotificationRequest);
            this.notificationService.sendNotificationToGarage(sendNotificationRequest);
        }
        return quotationId;
    }

    public BaseResponse<QuotationResponse> detailQuotation(Long repairOrderId, Long quotationId) throws ApiException {
        Optional<RepairOrder> optionalRepairOrder = repairOrderRepository.findById(repairOrderId);
        if (optionalRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, REPAIR_ORDER_NOT_EXISTS);
        }
        Optional<Quotation> optionalQuotation = quotationRepository.findById(quotationId);
        if (optionalQuotation.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, QUOTATION_NOT_EXISTS);
        }
        Quotation quotation = optionalQuotation.get();
        QuotationResponse quotationResponse = new QuotationResponse();
        quotationResponse.setId(quotation.getId());
        quotationResponse.setRepairOrderId(quotation.getRepairOrderId());
        quotationResponse.setDiagnoseId(quotation.getDiagnoseId());
        quotationResponse.setStatus(quotation.getStatus());

        List<QuotationProductDto> quotationInfos = quotationInfoRepository.findAllQuotationInfo(quotationId);

        List<QuotationLabourResponse> quotationLabours = new ArrayList<>();
        List<QuotationSparePartResponse> quotationSpareParts = new ArrayList<>();

        for (QuotationProductDto quotationInfo : quotationInfos) {
            if (quotationInfo.getProductType().equals(ProductTypeEnum.SERVICE.getCode())) {
                QuotationLabourResponse quotationLabour = new QuotationLabourResponse();
                quotationLabour.setQuotationId(quotationInfo.getQuotationId());
                quotationLabour.setGarageServiceId(quotationInfo.getGarageServiceId());
                quotationLabour.setQuantity(quotationInfo.getQuantity() == null ? BigDecimal.ZERO : quotationInfo.getQuantity());
                quotationLabour.setUnitPrice(quotationInfo.getUnitPrice());
                quotationLabour.setUnit(quotationInfo.getUnit());
                quotationLabour.setOriginPrice(quotationInfo.getOriginPrice());
                quotationLabour.setDiscount(quotationInfo.getDiscount());
                quotationLabour.setTax(quotationInfo.getTax());
                quotationLabour.setPrice(quotationInfo.getPrice());
                quotationLabour.setEmployeeId(quotationInfo.getEmployeeId());
                quotationLabour.setEmployeeName(quotationInfo.getEmployeeName());
                quotationLabours.add(quotationLabour);
            }
            if (quotationInfo.getProductType().equals(ProductTypeEnum.SPARE_PART.getCode())) {
                QuotationSparePartResponse quotationSparePart = new QuotationSparePartResponse();
                quotationSparePart.setQuotationId(quotationInfo.getQuotationId());
                quotationSparePart.setProductId(quotationInfo.getProductId());
                quotationSparePart.setProductCode(quotationInfo.getProductCode());
                quotationSparePart.setProductName(quotationInfo.getProductName());
                quotationSparePart.setQuantity(quotationSparePart.getQuantity() == null ? BigDecimal.ZERO : quotationSparePart.getQuantity());
                quotationSparePart.setUnitPrice(quotationInfo.getUnitPrice());
                quotationSparePart.setUnit(quotationInfo.getUnit());
                quotationSparePart.setOriginPrice(quotationInfo.getOriginPrice());
                quotationSparePart.setDiscount(quotationInfo.getDiscount());
                quotationSparePart.setTax(quotationInfo.getTax());
                quotationSparePart.setPrice(quotationInfo.getPrice());
                quotationSpareParts.add(quotationSparePart);
            }
        }

        quotationResponse.setQuotationLabours(quotationLabours);
        quotationResponse.setQuotationSpareParts(quotationSpareParts);
        BaseResponse<QuotationResponse> response = new BaseResponse<>();
        response.setData(quotationResponse);
        return response;
    }
}
