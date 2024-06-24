package vn.com.cardoctor.garage_service.services.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.orders.*;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;
import vn.com.cardoctor.garage_service.models.requests.quotation.QuotationSparePartGmsRequest;
import vn.com.cardoctor.garage_service.models.requests.repair_order.FilterRepairOrder;
import vn.com.cardoctor.garage_service.models.requests.repair_order.RepairOrderRequest;
import vn.com.cardoctor.garage_service.models.responses.diagnose.DiagnoseResponse;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationLabourResponse;
import vn.com.cardoctor.garage_service.models.responses.product.QuotationSparePartResponse;
import vn.com.cardoctor.garage_service.models.responses.quotation.PrintQuotationResponse;
import vn.com.cardoctor.garage_service.models.responses.quotation.QuotationResponse;
import vn.com.cardoctor.garage_service.models.responses.repair_order.CarResponse;
import vn.com.cardoctor.garage_service.models.responses.repair_order.CustomerResponse;
import vn.com.cardoctor.garage_service.models.responses.repair_order.PrintRepairOrderResponse;
import vn.com.cardoctor.garage_service.models.responses.repair_order.RepairOrderResponse;
import vn.com.cardoctor.garage_service.repositories.*;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders.*;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.services.GeneratorCodeTicketService;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class RepairOrderService extends BaseService {
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final DiagnoseRepository diagnoseRepository;
    private final QuotationRepository quotationRepository;
    private final QuotationInfoRepository quotationInfoRepository;
    private final QuotationProductLogRepository quotationProductLogRepository;
    private final GeneratorCodeTicketService generatorCodeTicketService;
    private final OutboundProductRepository outboundProductRepository;
    private final InboundProductRepository inboundProductRepository;
    private final OutboundTicketRepository outboundTicketRepository;
    private final InboundTicketRepository inboundTicketRepository;

    public RepairOrderResponse getDetailRepairOrderGms(Long repairOrderId) throws ApiException, JsonProcessingException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        RepairOrder repairOrder = oRepairOrder.get();
        RepairOrderResponse response = new RepairOrderResponse();
        CarResponse carResponse = new CarResponse();
        Optional<Car> oCar = this.carRepository.findById(repairOrder.getCarId());
        if (oCar.isPresent()) {
            Car car = oCar.get();
            carResponse.setCarId(car.getId());
            carResponse.setCarName(car.getCarName());
            carResponse.setCarBrandId(car.getCarBrandId());
            carResponse.setCarModelId(car.getCarModelId());
            carResponse.setCarYearId(car.getCarYearId());
            carResponse.setCarVersionId(car.getCarVersionId());
            carResponse.setLicensePlate(car.getLicensePlate());
            carResponse.setVinNumber(car.getVinNumber());
            carResponse.setDriverCarId(car.getDriverCarId());
        }
        CustomerResponse customerResponse = new CustomerResponse();
        Optional<Customer> oCustomer = this.customerRepository.findById(repairOrder.getCustomerId());
        if (oCustomer.isPresent()) {
            Customer customer = oCustomer.get();
            customerResponse.setFullName(customer.getFullName());
            customerResponse.setPhoneNumber(customer.getPhoneNumber());
            customerResponse.setCustomerTypeId(customer.getCustomerTypeId());
            customerResponse.setDriverId(customer.getDriverId());
            customerResponse.setAddress(customer.getAddress());
        }
        response.setCarResponse(carResponse);
        response.setCustomerResponse(customerResponse);
        response.setCode(repairOrder.getCode());
        response.setDescription(repairOrder.getDescription());
        response.setAppointmentDate(repairOrder.getAppointmentDate());
        response.setExpectedHandoverDate(repairOrder.getExpectedHandoverDate());
        response.setOdo(repairOrder.getOdo());
        response.setStatus(repairOrder.getStatus());
        response.setPaymentStatus(repairOrder.getPaymentStatus());
        response.setCanceledAt(repairOrder.getCanceledAt());
        response.setGarageId(repairOrder.getGarageId());
        response.setNote(repairOrder.getNote());

        DiagnoseResponse diagnoseResponse = new DiagnoseResponse();
        Optional<Diagnose> oDiagnose = this.diagnoseRepository.findByRepairOrderId(repairOrderId);
        oDiagnose.ifPresent(diagnose -> diagnoseResponse.setDescription(diagnose.getDescription()));
        response.setDiagnose(diagnoseResponse);

        QuotationResponse quotationResponse = new QuotationResponse();
        Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(repairOrderId);
        if (oQuotation.isPresent()) {
            quotationResponse.setId(oQuotation.get().getId());
            quotationResponse.setRepairOrderId(oQuotation.get().getRepairOrderId());
            quotationResponse.setStatus(oQuotation.get().getStatus());
            quotationResponse.setDiagnoseId(oQuotation.get().getDiagnoseId());
            quotationResponse.setDiscountType(oQuotation.get().getDiscountType());
            List<QuotationLabourResponse> quotationLabours = new ArrayList<>();
            List<QuotationProductDto> quotationInfos = quotationInfoRepository.findAllQuotationInfo(oQuotation.get().getId());
            for (QuotationProductDto quotationInfo : quotationInfos) {
                if (quotationInfo.getProductType().equals(ProductTypeEnum.SERVICE.getCode())) {
                    QuotationLabourResponse quotationLabour = getQuotationLabourResponse(quotationInfo);
                    quotationLabours.add(quotationLabour);
                }
            }
            List<QuotationSparePartResponse> quotationSpareParts = new ArrayList<>();
            String content = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(oQuotation.get().getId())
                    .orElse(new QuotationProductLog()).getContent();
            List<QuotationSparePartGmsRequest> quotationSparePartGmsRequests = new ObjectMapper().readValue(content, new TypeReference<>() {
            });
            for (QuotationSparePartGmsRequest quotationSparePartGmsRequest : quotationSparePartGmsRequests) {
                QuotationSparePartResponse quotationSparePartResponse = new QuotationSparePartResponse();
                quotationSparePartResponse.setQuotationId(oQuotation.get().getId());
                quotationSparePartResponse.setProductId(quotationSparePartGmsRequest.getProductId());
                quotationSparePartResponse.setProductName(quotationSparePartGmsRequest.getProductName());
                quotationSparePartResponse.setUnit(quotationSparePartGmsRequest.getUnit());
                quotationSparePartResponse.setQuantity(quotationSparePartGmsRequest.getQuantity());
                quotationSparePartResponse.setUnitPrice(quotationSparePartGmsRequest.getUnitPrice());
                quotationSparePartResponse.setDiscount(quotationSparePartGmsRequest.getDiscount());
                quotationSparePartResponse.setTax(quotationSparePartGmsRequest.getTax());
                quotationSparePartResponse.setOriginPrice(quotationSparePartGmsRequest.getOriginPrice());
                quotationSparePartResponse.setPrice(quotationSparePartGmsRequest.getPrice());
                quotationSparePartResponse.setTax(quotationSparePartGmsRequest.getTax());
                quotationSparePartResponse.setStatus(quotationSparePartGmsRequest.getStatus());
                quotationSparePartResponse.setOutboundProductId(quotationSparePartGmsRequest.getOutboundProductId());
                quotationSparePartResponse.setOutboundTicketId(quotationSparePartGmsRequest.getOutboundTicketId());
                quotationSpareParts.add(quotationSparePartResponse);
            }

            // Phụ tùng trước khi bấm lưu
            List<QuotationSparePartResponse> oldQuotationSpareParts = new ArrayList<>();
            for (QuotationProductDto quotationInfo : quotationInfos) {
                if (quotationInfo.getProductType().equals(ProductTypeEnum.SPARE_PART.getCode())) {
                    QuotationSparePartResponse quotationSparePart = getQuotationSparePartResponse(quotationInfo);
                    oldQuotationSpareParts.add(quotationSparePart);
                }
            }

            quotationResponse.setQuotationLabours(quotationLabours);
            quotationResponse.setOldQuotationSpareParts(oldQuotationSpareParts);
            quotationResponse.setQuotationSpareParts(quotationSpareParts);
        }
        response.setQuotation(quotationResponse);
        return response;
    }

    public PrintRepairOrderResponse getDetailRepairOrderForPrint(Long repairOrderId) throws ApiException, JsonProcessingException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        RepairOrder repairOrder = oRepairOrder.get();
        PrintRepairOrderResponse response = new PrintRepairOrderResponse();
        CarResponse carResponse = new CarResponse();
        Optional<Car> oCar = this.carRepository.findById(repairOrder.getCarId());
        if (oCar.isPresent()) {
            Car car = oCar.get();
            carResponse.setCarId(car.getId());
            carResponse.setCarName(car.getCarName());
            carResponse.setCarBrandId(car.getCarBrandId());
            carResponse.setCarModelId(car.getCarModelId());
            carResponse.setCarYearId(car.getCarYearId());
            carResponse.setCarVersionId(car.getCarVersionId());
            carResponse.setLicensePlate(car.getLicensePlate());
            carResponse.setVinNumber(car.getVinNumber());
            carResponse.setDriverCarId(car.getDriverCarId());
        }
        CustomerResponse customerResponse = new CustomerResponse();
        Optional<Customer> oCustomer = this.customerRepository.findById(repairOrder.getCustomerId());
        if (oCustomer.isPresent()) {
            Customer customer = oCustomer.get();
            customerResponse.setFullName(customer.getFullName());
            customerResponse.setPhoneNumber(customer.getPhoneNumber());
            customerResponse.setCustomerTypeId(customer.getCustomerTypeId());
            customerResponse.setDriverId(customer.getDriverId());
        }
        response.setCarResponse(carResponse);
        response.setCustomerResponse(customerResponse);
        response.setCode(repairOrder.getCode());
        response.setDescription(repairOrder.getDescription());
        response.setAppointmentDate(repairOrder.getAppointmentDate());
        response.setExpectedHandoverDate(repairOrder.getExpectedHandoverDate());
        response.setOdo(repairOrder.getOdo());
        response.setStatus(repairOrder.getStatus());
        response.setPaymentStatus(repairOrder.getPaymentStatus());
        response.setCanceledAt(repairOrder.getCanceledAt());
        response.setGarageId(repairOrder.getGarageId());
        response.setNote(repairOrder.getNote());

        DiagnoseResponse diagnoseResponse = new DiagnoseResponse();
        Optional<Diagnose> oDiagnose = this.diagnoseRepository.findByRepairOrderId(repairOrderId);
        oDiagnose.ifPresent(diagnose -> diagnoseResponse.setDescription(diagnose.getDescription()));
        response.setDiagnose(diagnoseResponse);

        PrintQuotationResponse printQuotationResponse = new PrintQuotationResponse();
        Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(repairOrderId);
        if (oQuotation.isPresent()) {
            printQuotationResponse.setId(oQuotation.get().getId());
            printQuotationResponse.setRepairOrderId(oQuotation.get().getRepairOrderId());
            printQuotationResponse.setStatus(oQuotation.get().getStatus());
            printQuotationResponse.setDiagnoseId(oQuotation.get().getDiagnoseId());
            List<QuotationLabourResponse> quotationLabours = new ArrayList<>();
            List<QuotationProductDto> quotationInfos = quotationInfoRepository.findAllQuotationInfo(oQuotation.get().getId());
            Map<String, List<QuotationLabourResponse>> quotationInfosMap = new HashMap<>();
            for (QuotationProductDto quotationInfo : quotationInfos) {
                if (quotationInfo.getProductType().equals(ProductTypeEnum.SERVICE.getCode())) {
                    QuotationLabourResponse quotationLabour = getQuotationLabourResponse(quotationInfo);
                    quotationLabours.add(quotationLabour);
                    if (!quotationInfosMap.containsKey(quotationLabour.getGarageServiceTypeName())) {
                        quotationInfosMap.put(quotationLabour.getGarageServiceTypeName(), new ArrayList<>());
                    }
                    quotationInfosMap.get(quotationLabour.getGarageServiceTypeName()).add(quotationLabour);
                }
            }
            List<QuotationSparePartResponse> quotationSpareParts = new ArrayList<>();
            String content = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(oQuotation.get().getId())
                    .orElse(new QuotationProductLog()).getContent();
            List<QuotationSparePartGmsRequest> quotationSparePartGmsRequests = new ObjectMapper().readValue(content, new TypeReference<>() {
            });
            for (QuotationSparePartGmsRequest quotationSparePartGmsRequest : quotationSparePartGmsRequests) {
                QuotationSparePartResponse quotationSparePartResponse = new QuotationSparePartResponse();
                quotationSparePartResponse.setQuotationId(oQuotation.get().getId());
                quotationSparePartResponse.setProductId(quotationSparePartGmsRequest.getProductId());
                quotationSparePartResponse.setUnit(quotationSparePartGmsRequest.getUnit());
                quotationSparePartResponse.setQuantity(quotationSparePartGmsRequest.getQuantity());
                quotationSparePartResponse.setUnitPrice(quotationSparePartGmsRequest.getUnitPrice());
                quotationSparePartResponse.setDiscount(quotationSparePartGmsRequest.getDiscount());
                quotationSparePartResponse.setTax(quotationSparePartGmsRequest.getTax());
                quotationSparePartResponse.setOriginPrice(quotationSparePartGmsRequest.getOriginPrice());
                quotationSparePartResponse.setPrice(quotationSparePartGmsRequest.getPrice());
                quotationSparePartResponse.setTax(quotationSparePartGmsRequest.getTax());
                quotationSparePartResponse.setStatus(quotationSparePartGmsRequest.getStatus());
                quotationSparePartResponse.setOutboundProductId(quotationSparePartGmsRequest.getOutboundProductId());
                quotationSparePartResponse.setOutboundTicketId(quotationSparePartGmsRequest.getOutboundTicketId());
                quotationSpareParts.add(quotationSparePartResponse);
            }

            printQuotationResponse.setQuotationLabours(quotationLabours);
            printQuotationResponse.setQuotationSpareParts(quotationSpareParts);
            printQuotationResponse.setPrintQuotationLabours(quotationInfosMap);
        }
        response.setQuotation(printQuotationResponse);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createRepairOrderGms(Long garageId, RepairOrderRequest request) throws ApiException {
        validCustomerInfo(request.getCustomerId(), request.getCustomerPhone(), request.getCustomerName(), request.getCustomerTypeId());
        validCarInfo(request.getCarId(), request.getLicensePlate());
        RepairOrder repairOrder = new RepairOrder();
        String code = this.generatorCodeTicketService.generateTicketCode(garageId, "DV");
        repairOrder.setCode(code);
        Customer customer;
        if (Objects.nonNull(request.getCustomerId())) {
            Optional<Customer> oCustomer = this.customerRepository.findById(request.getCustomerId());
            if (oCustomer.isEmpty()) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
            }
            customer = oCustomer.get();
        } else {
            customer = new Customer();
            customer.setGarageId(garageId);
            customer.setCustomerTypeId(request.getCustomerTypeId());
            customer.setFullName(request.getCustomerName());
            customer.setPhoneNumber(request.getCustomerPhone());
            try {
                customer = this.customerRepository.save(customer);
            } catch (Exception e) {
                throw new ApiException(ERROR.BAD_REQUEST, "Số điện thoại đã trùng với người khác, vui lòng nhập lại");
            }
        }
        repairOrder.setCustomerId(customer.getId());

        Car car;
        if (Objects.nonNull(request.getCarId())) {
            Optional<Car> oCar = this.carRepository.findById(request.getCarId());
            if (oCar.isEmpty()) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
            }
            car = oCar.get();
        } else {
            car = new Car();
            car.setCarName(request.getCarName());
            car.setCarBrandId(request.getCarBrandId());
            car.setCarModelId(request.getCarModelId());
            car.setCarYearId(request.getCarYearId());
            car.setCarVersionId(request.getCarVersionId());
            car.setLicensePlate(request.getLicensePlate());
            car.setVinNumber(request.getVinNumber());
            car.setCustomerId(customer.getId());
            car.setGarageId(garageId);
            try {
                car = this.carRepository.save(car);
            } catch (Exception e) {
                throw new ApiException(ERROR.BAD_REQUEST, "Biển số xe đã trùng với người khác, vui lòng nhập lại");
            }
        }
        String username = this.getKeyCloakUsername();
        if (username == null) {
            throw new ApiException(ERROR.BAD_REQUEST);
        }
        repairOrder.setCreatedBy(username);
        repairOrder.setCarId(car.getId());
        repairOrder.setDescription(request.getDescription());
        repairOrder.setStatus(RepairOrderStatus.RECEIVED.getCode());
        repairOrder.setPaymentStatus(PaymentCompletedStatus.WAIT_FOR_PAY.getCode());
        repairOrder.setOdo(request.getOdo());
        repairOrder.setGarageId(garageId);
        if (request.getAppointmentDate() == null) {
            repairOrder.setAppointmentDate(new Date());
        } else {
            repairOrder.setAppointmentDate(request.getAppointmentDate());
        }
        repairOrder.setExpectedHandoverDate(request.getExpectedHandoverDate());

        repairOrder = repairOrderRepository.save(repairOrder);
        return repairOrder.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long updateRepairOrderGms(Long garageId, Long repairOrderId, RepairOrderRequest request) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        validCustomerInfo(request.getCustomerId(), request.getCustomerPhone(), request.getCustomerName(), request.getCustomerTypeId());
        validCarInfo(request.getCarId(), request.getLicensePlate());
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setDescription(request.getDescription());
        repairOrder.setOdo(request.getOdo());
        repairOrder = repairOrderRepository.save(repairOrder);
        return repairOrder.getId();
    }

    public PagingDataResponse getAllRepairOrders(FilterRepairOrder filterRepairOrder) {
        return this.repairOrderRepository.getAllRepairOrder(filterRepairOrder);
    }

    public PagingDataResponse adminGetAllRepairOrder(String code, String garageOwnerPhone, String garageName, Date fromDate, Date toDate,
                                                     Integer pageNumber, Integer pageSize) {
        return this.repairOrderRepository.adminGetAllRepairOrder(code, garageOwnerPhone, garageName, fromDate, toDate, pageNumber, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long changeQuotationStatus(Long repairOrderId) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setStatus(RepairOrderStatus.QUOTATION_SEND.getCode());
        this.repairOrderRepository.save(repairOrder);
        return repairOrderId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long completeRepair(Long repairOrderId) throws ApiException, JsonProcessingException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(repairOrderId);
        if (oQuotation.isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST, "Chưa có báo giá, không thể hoàn thành đơn");
        }
        Optional<QuotationProductLog> oQuotationProductLog = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(oQuotation.get().getId());
        if (oQuotationProductLog.isEmpty()) {
            throw new ApiException(ERROR.BAD_REQUEST);
        }
        List<QuotationInfo> quotationInfos = new ArrayList<>();

        QuotationProductLog quotationProductLog = oQuotationProductLog.get();
        String content = quotationProductLog.getContent();
        List<QuotationSparePartGmsRequest> quotationSparePartGmsRequests = new ObjectMapper().readValue(content, new TypeReference<List<QuotationSparePartGmsRequest>>() {
        });
        this.quotationInfoRepository.deleteByQuotationIdAndType(oQuotation.get().getId(), QuotationTypeEnum.SPARE_PART.getCode());
        for (QuotationSparePartGmsRequest quotationSparePartGmsRequest : quotationSparePartGmsRequests) {
            if (quotationSparePartGmsRequest.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                throw new ApiException(ERROR.BAD_REQUEST, "Còn tồn tại sản phẩm chưa được xuất kho");
            }
            QuotationInfo quotationInfo = new QuotationInfo();
            quotationInfo.setQuotationId(oQuotation.get().getId());
            quotationInfo.setType(QuotationTypeEnum.SPARE_PART.getCode());
            quotationInfo.setProductId(quotationSparePartGmsRequest.getProductId());
            quotationInfo.setUnitPrice(quotationSparePartGmsRequest.getUnitPrice());
            quotationInfo.setQuantity(quotationSparePartGmsRequest.getQuantity());
            quotationInfo.setDiscount(quotationSparePartGmsRequest.getDiscount());
            quotationInfo.setTax(quotationSparePartGmsRequest.getTax());
            BigDecimal originPrice = quotationSparePartGmsRequest.getUnitPrice().multiply(quotationSparePartGmsRequest.getQuantity());
            BigDecimal price = originPrice.subtract(quotationSparePartGmsRequest.getDiscount()).multiply(new BigDecimal(1).add(quotationSparePartGmsRequest.getTax()));
            quotationInfo.setOriginPrice(originPrice);
            quotationInfo.setPrice(price);
            quotationInfo.setStatus(quotationSparePartGmsRequest.getStatus());
            quotationInfo.setOutboundProductId(quotationSparePartGmsRequest.getOutboundProductId());
            quotationInfos.add(quotationInfo);
        }
        this.quotationInfoRepository.saveAll(quotationInfos);
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setStatus(RepairOrderStatus.COMPLETE_WORK_ORDER.getCode());
        this.repairOrderRepository.save(repairOrder);
        return repairOrderId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long handoverCar(Long repairOrderId) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setStatus(RepairOrderStatus.DONE.getCode());
        this.repairOrderRepository.save(repairOrder);
        return repairOrderId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long cancel(Long repairOrderId) throws ApiException {
        Optional<RepairOrder> oRepairOrder = this.repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setStatus(RepairOrderStatus.CUSTOMER_CANCELED.getCode());
        repairOrder.setCanceledAt(new Date());

        Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(repairOrderId);
        if (oQuotation.isPresent()) {
            List<QuotationSparePartGmsRequest> quotationSparePartGmsRequests = new ArrayList<>();
            List<QuotationProductDto> quotationProductDtos = this.quotationInfoRepository.findAllQuotationInfo(oQuotation.get().getId());
            for (QuotationProductDto quotationProductDto : quotationProductDtos) {
                if (quotationProductDto.getProductType().equals(ProductTypeEnum.SPARE_PART.getCode())) {
                    QuotationSparePartGmsRequest quotationSparePartGmsRequest = new QuotationSparePartGmsRequest();
                    quotationSparePartGmsRequest.setProductId(quotationProductDto.getProductId());
                    quotationSparePartGmsRequest.setUnit(quotationProductDto.getUnit());
                    quotationSparePartGmsRequest.setQuantity(quotationProductDto.getQuantity());
                    quotationSparePartGmsRequest.setUnitPrice(quotationProductDto.getUnitPrice());
                    quotationSparePartGmsRequest.setDiscount(quotationProductDto.getDiscount());
                    quotationSparePartGmsRequest.setTax(quotationProductDto.getTax());
                    quotationSparePartGmsRequest.setPrice(quotationProductDto.getPrice());
                    quotationSparePartGmsRequest.setOriginPrice(quotationProductDto.getOriginPrice());
                    quotationSparePartGmsRequest.setStatus(quotationProductDto.getStatus());
                    quotationSparePartGmsRequest.setOutboundProductId(quotationProductDto.getOutboundProductId());
                    quotationSparePartGmsRequests.add(quotationSparePartGmsRequest);
                }
            }
            List<InboundProduct> inboundProducts = new ArrayList<>();
            if (!quotationSparePartGmsRequests.isEmpty()) {
                for (QuotationSparePartGmsRequest quotationSparePartGmsRequest : quotationSparePartGmsRequests) {
                    // Nếu phụ tùng chưa xuất kho -> Huỷ phiếu
                    if (quotationSparePartGmsRequest.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                        Optional<OutboundProduct> oOutboundProduct = this.outboundProductRepository.findById(quotationSparePartGmsRequest.getOutboundProductId());
                        OutboundProduct outboundProduct = oOutboundProduct.orElse(new OutboundProduct());
                        Optional<OutboundTicket> oOutboundTicket = this.outboundTicketRepository.findById(outboundProduct.getOutboundTicketId());
                        OutboundTicket outboundTicket = oOutboundTicket.orElse(new OutboundTicket());
                        outboundTicket.setStatus(InOutboundStatus.DELETED.getCode());
                        this.outboundTicketRepository.save(outboundTicket);
                    }
                    // Nếu phụ tùng đã xuất kho -> Tạo phiếu nhập kho
                    // Tạo thành 1 list sản phẩm để nhập kho
                    if (quotationSparePartGmsRequest.getStatus().equals(OutboundProductStatus.DA_XUAT.getCode())) {
                        InboundProduct inboundProduct = new InboundProduct();
                        inboundProduct.setProductId(quotationSparePartGmsRequest.getProductId());
                        inboundProduct.setUnit(quotationSparePartGmsRequest.getUnit());
                        inboundProduct.setRequestQuantity(quotationSparePartGmsRequest.getQuantity());
                        inboundProduct.setExportQuantity(BigDecimal.ZERO);
                        inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                        inboundProducts.add(inboundProduct);
                    }
                }
            }
            // Nếu có sản phẩm đã xuất kho -> Tạo phiếu nhập kho
            if (!inboundProducts.isEmpty()) {
                InboundTicket inboundTicket = new InboundTicket();
                String code = repairOrder.getCode();
                String[] splitedCode = code.split("DV");
                String inboundCode = splitedCode[0] + "NK" + InboundType.REFUND_REPAIR_ORDER.getCode() + splitedCode[1];
                int numberOfInbound = this.inboundTicketRepository.countInboundTicketByTicketIdAndType(repairOrderId, InboundType.REFUND_REPAIR_ORDER.getCode()) + 1;
                inboundCode = inboundCode + "-" + String.format("%02d", numberOfInbound);
                inboundTicket.setCode(inboundCode);
                inboundTicket.setType(InboundType.REFUND_REPAIR_ORDER.getCode());
                inboundTicket.setTicketId(repairOrderId);
                inboundTicket.setGarageId(repairOrder.getGarageId());
                inboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                inboundTicket = this.inboundTicketRepository.save(inboundTicket);
                for (InboundProduct inboundProduct : inboundProducts) {
                    inboundProduct.setInboundTicketId(inboundTicket.getId());
                }
                this.inboundProductRepository.saveAll(inboundProducts);
            }
        }

        this.repairOrderRepository.save(repairOrder);
        return repairOrderId;
    }

    public Long changePaymentStatus(Long repairOrderId, int paymentCompleteStatus) throws ApiException {
        Optional<RepairOrder> oRepairOrder = repairOrderRepository.findById(repairOrderId);
        if (oRepairOrder.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        validPaymentStatus(paymentCompleteStatus);
        RepairOrder repairOrder = oRepairOrder.get();
        repairOrder.setPaymentStatus(paymentCompleteStatus);
        repairOrderRepository.save(repairOrder);
        return repairOrderId;
    }


    private void validCustomerInfo(Long customerId, String customerPhone, String customerName, Long customerTypeId) throws ApiException {
        if (Objects.isNull(customerId) && Objects.isNull(customerPhone)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin cá nhân không được để trống");
        }
        if (Objects.isNull(customerId)) {
            if (Objects.isNull(customerName)) {
                throw new ApiException(ERROR.BAD_REQUEST, "Thiếu thông tin tên khách hàng");
            }
            if (Objects.isNull(customerTypeId)) {
                throw new ApiException(ERROR.BAD_REQUEST, "Vui lòng thêm khách hàng vào nhóm khách hàng của bạn");
            }
        }
    }

    private void validCarInfo(Long carId, String licensePlate) throws ApiException {
        if (Objects.isNull(carId) && Objects.isNull(licensePlate)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin xe không được để trống");
        }
        if (!StringUtils.hasText(licensePlate)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin biển số xe không được để trống");
        }
    }

    public void validRepairOrderStatus(int repairOrderStatusRequest) throws ApiException {
        RepairOrderStatus repairOrderStatus = RepairOrderStatus.getRepairOrderStatus(repairOrderStatusRequest);
        if (repairOrderStatus == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin trạng thái sửa chữa không đúng");
        }
    }

    public void validPaymentStatus(int paymentStatus) throws ApiException {
        PaymentCompletedStatus paymentCompletedStatus = PaymentCompletedStatus.getPaymentCompletedStatus(paymentStatus);
        if (paymentCompletedStatus == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin trạng thái thanh toán không đúng");
        }
    }

    private static QuotationLabourResponse getQuotationLabourResponse(QuotationProductDto quotationInfo) {
        QuotationLabourResponse quotationLabour = new QuotationLabourResponse();
        quotationLabour.setQuotationId(quotationInfo.getQuotationId());
        quotationLabour.setGarageServiceId(quotationInfo.getGarageServiceId());
        quotationLabour.setGarageServiceTypeId(quotationInfo.getGarageServiceTypeId());
        quotationLabour.setGarageServiceTypeName(quotationInfo.getGarageServiceTypeName());
        quotationLabour.setQuantity(quotationInfo.getQuantity());
        quotationLabour.setUnitPrice(quotationInfo.getUnitPrice());
        quotationLabour.setUnit(quotationInfo.getUnit());
        quotationLabour.setOriginPrice(quotationInfo.getOriginPrice());
        quotationLabour.setDiscount(quotationInfo.getDiscount());
        quotationLabour.setTax(quotationInfo.getTax());
        quotationLabour.setPrice(quotationInfo.getPrice());
        quotationLabour.setEmployeeId(quotationInfo.getEmployeeId());
        quotationLabour.setEmployeeName(quotationInfo.getEmployeeName());
        quotationLabour.setOutboundProductId(quotationInfo.getOutboundProductId());
        return quotationLabour;
    }

    private static QuotationSparePartResponse getQuotationSparePartResponse(QuotationProductDto quotationInfo) {
        QuotationSparePartResponse quotationSparePart = new QuotationSparePartResponse();
        quotationSparePart.setQuotationId(quotationInfo.getQuotationId());
        quotationSparePart.setProductId(quotationInfo.getProductId());
        quotationSparePart.setProductCode(quotationInfo.getProductCode());
        quotationSparePart.setProductName(quotationInfo.getProductName());
        quotationSparePart.setQuantity(quotationInfo.getQuantity());
        quotationSparePart.setUnitPrice(quotationInfo.getUnitPrice());
        quotationSparePart.setUnit(quotationInfo.getUnit());
        quotationSparePart.setOriginPrice(quotationInfo.getOriginPrice());
        quotationSparePart.setDiscount(quotationInfo.getDiscount());
        quotationSparePart.setTax(quotationInfo.getTax());
        quotationSparePart.setPrice(quotationInfo.getPrice());
        quotationSparePart.setStatus(quotationInfo.getStatus());
        quotationSparePart.setOutboundProductId(quotationInfo.getOutboundProductId());
        return quotationSparePart;
    }

    public BigDecimal getNumberServiceByStatus(Long garageId, Integer status, String fromDate, String toDate) {
        return repairOrderRepository.getNumberServiceByStatus(garageId, status, fromDate, toDate);
    }

    public BigDecimal getRevenue(Long garageId, List<Integer> statusCancelRepairOrder, String fromDate, String toDate) {
        return repairOrderRepository.getRevenue(garageId, statusCancelRepairOrder, fromDate, toDate);
    }
}
