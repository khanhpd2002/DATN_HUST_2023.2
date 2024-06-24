package vn.com.cardoctor.garage_service.services.in_out_bounds;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.OutboundProduct;
import vn.com.cardoctor.garage_service.entities.OutboundTicket;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.entities.ProductLog;
import vn.com.cardoctor.garage_service.entities.orders.Quotation;
import vn.com.cardoctor.garage_service.entities.orders.QuotationInfo;
import vn.com.cardoctor.garage_service.entities.orders.QuotationProductLog;
import vn.com.cardoctor.garage_service.entities.orders_part.OrderDistributor;
import vn.com.cardoctor.garage_service.entities.orders_part.SellSparePart;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.events.EventPublisher;
import vn.com.cardoctor.garage_service.models.dtos.QuotationProductDto;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.FlagRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.OutboundReferenceProductRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.OutboundTicketRequest;
import vn.com.cardoctor.garage_service.models.responses.in_out_bound.OutboundTicketResponse;
import vn.com.cardoctor.garage_service.repositories.ProductLogRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders.QuotationInfoRepository;
import vn.com.cardoctor.garage_service.repositories.orders.QuotationProductLogRepository;
import vn.com.cardoctor.garage_service.repositories.orders.QuotationRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.OrderDistributorRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.SellSparePartRepository;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OutboundService extends BaseService {
    private final OutboundTicketRepository outboundTicketRepository;
    private final OutboundProductRepository outboundProductRepository;
    private final QuotationRepository quotationRepository;
    private final QuotationProductLogRepository quotationProductLogRepository;
    private final ProductRepository productRepository;
    private final QuotationInfoRepository quotationInfoRepository;
    private final SellSparePartRepository sellSparePartRepository;
    private final ProductLogRepository productLogRepository;
    private final OrderDistributorRepository orderDistributorRepository;

    public PagingDataResponse findAllOutboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                                    Integer pageSize, Integer pageNumber) {
        return this.outboundTicketRepository.findAllOutboundTicket(garageId, fromDate, toDate, type, code, status, pageSize, pageNumber);
    }

    public OutboundTicketResponse getDetailOutboundTicket(Long outboundTicketId) throws ApiException {
        Optional<OutboundTicket> oOutboundTicket = this.outboundTicketRepository.findById(outboundTicketId);
        if (oOutboundTicket.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        OutboundTicket outboundTicket = oOutboundTicket.get();
        OutboundTicketResponse response = new OutboundTicketResponse();
        response.setId(outboundTicket.getId());
        response.setType(outboundTicket.getType());
        response.setCode(outboundTicket.getCode());
        response.setCreatedAt(outboundTicket.getCreatedAt());
        response.setTicketId(outboundTicket.getTicketId());
        response.setStatus(outboundTicket.getStatus());
        response.setDistributorId(outboundTicket.getDistributorId());

        List<OutboundProduct> outboundProducts = this.outboundProductRepository.findByOutboundTicketId(outboundTicketId);
        response.setOutboundProducts(outboundProducts);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long confirmAction(Long garageId, Long outboundTicketId, OutboundTicketRequest request) throws ApiException, JsonProcessingException {
        FlagRequest flagRequest = new FlagRequest(false, false, true);
        Optional<OutboundTicket> oOutboundTicket = this.outboundTicketRepository.findById(outboundTicketId);
        if (oOutboundTicket.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        OutboundTicket outboundTicket = oOutboundTicket.get();
        if (outboundTicket.getStatus().equals(InOutboundStatus.VERIFIED.getCode())) {
            throw new ApiException(ERROR.BAD_REQUEST, "Could not update");
        }
        Optional<QuotationProductLog> oQuotationProductLog;
        QuotationProductLog quotationProductLog = new QuotationProductLog();
        Quotation quotation = new Quotation();
        SellSparePart sellSparePart = new SellSparePart();
        OrderDistributor orderDistributor;

        // Tương tác với log
        List<OutboundReferenceProductRequest> outboundReferenceProductRequests = new ArrayList<>();
        List<OutboundReferenceProductRequest> newOutboundReferenceProductRequests = new ArrayList<>();

        // Tuơng tác với phiếu chính trước khi bấm Lưu tạm thời
        List<OutboundReferenceProductRequest> currentOutboundReferenceProductRequests = new ArrayList<>();
        List<OutboundReferenceProductRequest> currentNewOutboundReferenceProductRequests = new ArrayList<>();

        if (outboundTicket.getType().equals(OutboundType.FOR_REPAIR_ORDER.getCode())) {
            flagRequest.setIsSellSparePart(false);
            Optional<Quotation> oQuotation = this.quotationRepository.findByRepairOrderId(outboundTicket.getTicketId());
            quotation = oQuotation.orElse(new Quotation());
            oQuotationProductLog = this.quotationProductLogRepository.findFirstByQuotationIdOrderByLogVersionDesc(quotation.getId());
            quotationProductLog = oQuotationProductLog.orElse(new QuotationProductLog());
            String content = quotationProductLog.getContent();
            outboundReferenceProductRequests = new ObjectMapper().readValue(content, new TypeReference<>() {
            });
            newOutboundReferenceProductRequests = new ArrayList<>(outboundReferenceProductRequests);
            List<QuotationProductDto> quotationProductDtos = this.quotationInfoRepository.findAllQuotationInfo(quotation.getId());
            for (QuotationProductDto quotationProductDto : quotationProductDtos) {
                if (quotationProductDto.getProductType().equals(ProductTypeEnum.SPARE_PART.getCode())) {
                    OutboundReferenceProductRequest outboundReferenceProductRequest = new OutboundReferenceProductRequest();
                    outboundReferenceProductRequest.setProductId(quotationProductDto.getProductId());
                    outboundReferenceProductRequest.setUnit(quotationProductDto.getUnit());
                    outboundReferenceProductRequest.setQuantity(quotationProductDto.getQuantity());
                    outboundReferenceProductRequest.setUnitPrice(quotationProductDto.getUnitPrice());
                    outboundReferenceProductRequest.setDiscount(quotationProductDto.getDiscount());
                    outboundReferenceProductRequest.setTax(quotationProductDto.getTax());
                    outboundReferenceProductRequest.setOriginalPrice(quotationProductDto.getOriginPrice());
                    outboundReferenceProductRequest.setPrice(quotationProductDto.getPrice());
                    outboundReferenceProductRequest.setStatus(quotationProductDto.getStatus());
                    outboundReferenceProductRequest.setOutboundProductId(quotationProductDto.getOutboundProductId());
                    currentOutboundReferenceProductRequests.add(outboundReferenceProductRequest);
                }
            }
            currentNewOutboundReferenceProductRequests = new ArrayList<>(currentOutboundReferenceProductRequests);
        }
        if (outboundTicket.getType().equals(OutboundType.FOR_SELL_SPARE_PART.getCode())) {
            Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(outboundTicket.getTicketId());
            sellSparePart = oSellSparePart.orElse(new SellSparePart());
            String content = sellSparePart.getProducts();
            outboundReferenceProductRequests = new ObjectMapper().readValue(content, new TypeReference<>() {
            });
            newOutboundReferenceProductRequests = new ArrayList<>(outboundReferenceProductRequests);
        }
        if (outboundTicket.getType().equals(OutboundType.FOR_REFUND.getCode())) {
            flagRequest.setIsSellSparePart(false);
        }

        List<OutboundProduct> outboundProducts = this.outboundProductRepository.findByOutboundTicketId(outboundTicketId);
        List<OutboundProduct> newOutboundProducts = new ArrayList<>();
        this.handleOnConfirmOutbound(outboundTicket, outboundProducts, request, newOutboundProducts, outboundReferenceProductRequests,
                newOutboundReferenceProductRequests, currentOutboundReferenceProductRequests, currentNewOutboundReferenceProductRequests, flagRequest);

        if (Boolean.TRUE.equals(flagRequest.getIsUpdate())) {
            outboundTicket.setStatus(InOutboundStatus.VERIFIED.getCode());
        }
        if (flagRequest.getIsCreateNewOutbound() && flagRequest.getIsUpdate()) {
            OutboundTicket newOutboundTicket = new OutboundTicket();
            String[] oldCode = outboundTicket.getCode().split("-");
            String code = oldCode[1];
            String newCode = oldCode[0] + "-" + StringUtil.getNextValue(code);
            newOutboundTicket.setCode(newCode);
            newOutboundTicket.setType(outboundTicket.getType());
            newOutboundTicket.setDistributorId(outboundTicket.getDistributorId());
            newOutboundTicket.setTicketId(outboundTicket.getTicketId());
            newOutboundTicket.setNote(outboundTicket.getNote());
            newOutboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
            newOutboundTicket.setGarageId(garageId);
            newOutboundTicket = this.outboundTicketRepository.save(newOutboundTicket);
            log.info("newOutboundProducts {}", newOutboundProducts.toString());
            log.info("newOutboundReferenceProductRequests {}", newOutboundReferenceProductRequests.toString());
            for (OutboundProduct outboundProduct : newOutboundProducts) {
                outboundProduct.setOutboundTicketId(newOutboundTicket.getId());
                for (OutboundReferenceProductRequest outboundReferenceProductRequest : newOutboundReferenceProductRequests) {
                    if (outboundReferenceProductRequest.getOutboundProductId().equals(outboundProduct.getId())) {
                        outboundReferenceProductRequest.setOutboundTicketId(newOutboundTicket.getId());
                    }
                }
                for (OutboundReferenceProductRequest currentOutboundReferenceProductRequest : currentNewOutboundReferenceProductRequests) {
                    if (currentOutboundReferenceProductRequest.getOutboundProductId().equals(outboundProduct.getId())) {
                        currentOutboundReferenceProductRequest.setOutboundTicketId(newOutboundTicket.getId());
                    }
                }
            }
            this.outboundProductRepository.saveAll(newOutboundProducts);
        }
        String listProductStr = new ObjectMapper().writeValueAsString(newOutboundReferenceProductRequests);

        // Nếu phiếu xuất kho ứng với dịch vụ
        // Tạo mới log + update vào Chi tiết phụ tùng báo giá
        if (outboundTicket.getType().equals(OutboundType.FOR_REPAIR_ORDER.getCode())) {
            quotationProductLog.setContent(listProductStr);
            // Tạo thêm log mới
            QuotationProductLog newQuotationProductLog = new QuotationProductLog();
            newQuotationProductLog.setQuotationId(quotationProductLog.getQuotationId());
            newQuotationProductLog.setLogVersion(quotationProductLog.getLogVersion() + 1);
            newQuotationProductLog.setContent(listProductStr);
            this.quotationProductLogRepository.save(newQuotationProductLog);

            // Update lại sản phẩm trong quotation_infos
            this.quotationInfoRepository.deleteByQuotationIdAndType(quotation.getId(), QuotationTypeEnum.SPARE_PART.getCode());
            List<QuotationInfo> quotationInfos = new ArrayList<>();
            for (OutboundReferenceProductRequest newOutboundReferenceProductRequest : currentNewOutboundReferenceProductRequests) {
                QuotationInfo quotationInfo = new QuotationInfo();
                quotationInfo.setQuotationId(quotation.getId());
                quotationInfo.setType(QuotationTypeEnum.SPARE_PART.getCode());
                quotationInfo.setProductId(newOutboundReferenceProductRequest.getProductId());
                quotationInfo.setUnitPrice(newOutboundReferenceProductRequest.getUnitPrice());
                quotationInfo.setQuantity(newOutboundReferenceProductRequest.getQuantity());
                quotationInfo.setDiscount(newOutboundReferenceProductRequest.getDiscount());
                quotationInfo.setTax(newOutboundReferenceProductRequest.getTax());
                BigDecimal originPrice = newOutboundReferenceProductRequest.getUnitPrice().multiply(newOutboundReferenceProductRequest.getQuantity());
                BigDecimal price = originPrice.subtract(newOutboundReferenceProductRequest.getDiscount()).multiply(new BigDecimal(1).add(newOutboundReferenceProductRequest.getTax()));
                quotationInfo.setOriginPrice(originPrice);
                quotationInfo.setPrice(price);
                quotationInfo.setStatus(newOutboundReferenceProductRequest.getStatus());
                quotationInfo.setOutboundProductId(newOutboundReferenceProductRequest.getOutboundProductId());
                quotationInfos.add(quotationInfo);
            }
            this.quotationInfoRepository.saveAll(quotationInfos);
        }

        // Nếu phiếu xuất kho ứng với đơn bán hàng
        // Lưu lại thông tin vào products trong phiếu bán
        if (outboundTicket.getType().equals(OutboundType.FOR_SELL_SPARE_PART.getCode())) {
            sellSparePart.setProducts(listProductStr);
            this.sellSparePartRepository.save(sellSparePart);
        }

        this.outboundProductRepository.saveAll(outboundProducts);
        this.outboundTicketRepository.save(outboundTicket);
        return outboundTicketId;
    }

    public void handleOnConfirmOutbound(OutboundTicket outboundTicket, List<OutboundProduct> outboundProducts, OutboundTicketRequest request,
                                        List<OutboundProduct> newOutboundProducts, List<OutboundReferenceProductRequest> outboundReferenceProductRequests, List<OutboundReferenceProductRequest> newOutboundReferenceProductRequests,
                                        List<OutboundReferenceProductRequest> currentOutboundReferenceProductRequests, List<OutboundReferenceProductRequest> currentNewOutboundReferenceProductRequests,
                                        FlagRequest flagRequest) throws ApiException {
        String actionUser = this.getKeyCloakUserId();
        for (int i = 0; i < request.getOutboundProducts().size(); i++) {
            if (request.getOutboundProducts().get(i).getExportQuantity() == null) {
                throw new ApiException(ERROR.BAD_REQUEST, "Vui lòng nhập số lượng");
            }
            // Số lượng thực tế > số lượng yêu cầu -> Exception
            if (outboundProducts.get(i).getRequestQuantity().compareTo(request.getOutboundProducts().get(i).getExportQuantity()) < 0) {
                throw new ApiException(ERROR.BAD_REQUEST, "Số lượng xuất thực tế không được nhiều hơn số lượng yêu cầu");
            }
            outboundProducts.get(i).setExportQuantity(request.getOutboundProducts().get(i).getExportQuantity());
            outboundProducts.get(i).setNote(request.getOutboundProducts().get(i).getNote());
            outboundProducts.get(i).setStatus(OutboundProductStatus.DA_XUAT.getCode());

            // Nếu có thay đổi số lượng thực tế -> Đánh dấu là đã update -> Sẽ close phiếu sau khi update
            if (request.getOutboundProducts().get(i).getExportQuantity().compareTo(BigDecimal.ZERO) > 0) {
                flagRequest.setIsUpdate(true);
            }

            // Nếu SL thực tế < SL yêu cầu -> Save lại thông tin mới với SL yêu cầu = SL yêu cầu cũ - SL đã xuất thực tế
            if (outboundProducts.get(i).getRequestQuantity().compareTo(request.getOutboundProducts().get(i).getExportQuantity()) > 0) {
                OutboundProduct outboundProduct = new OutboundProduct();
                outboundProduct.setProductId(outboundProducts.get(i).getProductId());
                outboundProduct.setUnit(outboundProducts.get(i).getUnit());
                outboundProduct.setDistributorId(outboundProducts.get(i).getDistributorId());
                outboundProduct.setRequestQuantity(outboundProducts.get(i).getRequestQuantity().subtract(request.getOutboundProducts().get(i).getExportQuantity()));
                outboundProduct.setExportQuantity(BigDecimal.ZERO);
                outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                outboundProduct = this.outboundProductRepository.save(outboundProduct);
                flagRequest.setIsCreateNewOutbound(true);

                // Tương tác với log
                for (OutboundReferenceProductRequest outboundReferenceProductRequest : outboundReferenceProductRequests) {
                    if (outboundReferenceProductRequest.getOutboundProductId().equals(outboundProducts.get(i).getId())) {
                        // Thay đổi số lượng trong phiếu dịch vụ, set lại trạng thái và tổng tiền
                        if (!outboundProducts.get(i).getExportQuantity().equals(BigDecimal.ZERO)) {
                            OutboundReferenceProductRequest productRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                productRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            productRequest.setProductId(outboundReferenceProductRequest.getProductId());
                            productRequest.setUnit(outboundReferenceProductRequest.getUnit());
                            productRequest.setQuantity(request.getOutboundProducts().get(i).getExportQuantity());
                            productRequest.setUnitPrice(outboundReferenceProductRequest.getUnitPrice());
                            productRequest.setDiscount(outboundReferenceProductRequest.getDiscount());
                            productRequest.setTax(outboundReferenceProductRequest.getTax());
                            productRequest.setOriginalPrice(outboundReferenceProductRequest.getUnitPrice().multiply(productRequest.getQuantity()));
                            productRequest.setPrice(calcTotalPrice(productRequest.getUnitPrice(), productRequest.getQuantity(),
                                    productRequest.getDiscount(), productRequest.getTax()));
                            productRequest.setStatus(OutboundProductStatus.DA_XUAT.getCode());
                            productRequest.setOutboundProductId(outboundReferenceProductRequest.getOutboundProductId());
                            productRequest.setOutboundTicketId(outboundReferenceProductRequest.getOutboundTicketId());
                            newOutboundReferenceProductRequests.add(productRequest);

                            newOutboundReferenceProductRequests.remove(outboundReferenceProductRequest);
                            // Thêm mới vật tư trong phiếu dịch vụ với số lượng bằng phần chưa xuất kho, trạng thái chưa xuất
                            OutboundReferenceProductRequest newOutboundReferenceProductRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                newOutboundReferenceProductRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            newOutboundReferenceProductRequest.setProductId(outboundReferenceProductRequest.getProductId());
                            newOutboundReferenceProductRequest.setUnit(outboundReferenceProductRequest.getUnit());
                            newOutboundReferenceProductRequest.setQuantity(outboundProducts.get(i).getRequestQuantity().subtract(request.getOutboundProducts().get(i).getExportQuantity()));
                            newOutboundReferenceProductRequest.setUnitPrice(outboundReferenceProductRequest.getUnitPrice());
                            newOutboundReferenceProductRequest.setDiscount(outboundReferenceProductRequest.getDiscount());
                            newOutboundReferenceProductRequest.setTax(outboundReferenceProductRequest.getTax());
                            newOutboundReferenceProductRequest.setOriginalPrice(newOutboundReferenceProductRequest.getUnitPrice().multiply(newOutboundReferenceProductRequest.getQuantity()));
                            newOutboundReferenceProductRequest.setPrice(calcTotalPrice(newOutboundReferenceProductRequest.getUnitPrice(), newOutboundReferenceProductRequest.getQuantity(),
                                    newOutboundReferenceProductRequest.getDiscount(), newOutboundReferenceProductRequest.getTax()));
                            newOutboundReferenceProductRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                            newOutboundReferenceProductRequest.setOutboundProductId(outboundProduct.getId());
                            newOutboundReferenceProductRequest.setOutboundTicketId(outboundProduct.getOutboundTicketId());
                            newOutboundReferenceProductRequests.add(newOutboundReferenceProductRequest);
                        } else {
                            OutboundReferenceProductRequest productRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                productRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            productRequest.setProductId(outboundReferenceProductRequest.getProductId());
                            productRequest.setUnit(outboundReferenceProductRequest.getUnit());
                            productRequest.setQuantity(request.getOutboundProducts().get(i).getRequestQuantity());
                            productRequest.setUnitPrice(outboundReferenceProductRequest.getUnitPrice());
                            productRequest.setDiscount(outboundReferenceProductRequest.getDiscount());
                            productRequest.setTax(outboundReferenceProductRequest.getTax());
                            productRequest.setOriginalPrice(outboundReferenceProductRequest.getUnitPrice().multiply(outboundReferenceProductRequest.getQuantity()));
                            productRequest.setPrice(calcTotalPrice(outboundReferenceProductRequest.getUnitPrice(), outboundReferenceProductRequest.getQuantity(),
                                    outboundReferenceProductRequest.getDiscount(), outboundReferenceProductRequest.getTax()));
                            productRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                            productRequest.setOutboundProductId(outboundProduct.getId());
                            productRequest.setOutboundTicketId(outboundProduct.getOutboundTicketId());
                            newOutboundReferenceProductRequests.add(productRequest);

                            newOutboundReferenceProductRequests.remove(outboundReferenceProductRequest);
                        }
                    }
                }
                newOutboundProducts.add(outboundProduct);

                // Tương tác với phiếu chính hiện tại trước khi bấm Lưu
                for (OutboundReferenceProductRequest currentOutboundReferenceProductRequest : currentOutboundReferenceProductRequests) {
                    log.info("currentOutboundReferenceProductRequests {}", currentOutboundReferenceProductRequests.toString());
                    log.info("======== currentOutboundReferenceProductRequest is ======= {}", currentOutboundReferenceProductRequest.toString());
                    if (currentOutboundReferenceProductRequest.getOutboundProductId().equals(outboundProducts.get(i).getId())) {
                        // Thay đổi số lượng trong phiếu dịch vụ, set lại trạng thái và tổng tiền
                        if (!outboundProducts.get(i).getExportQuantity().equals(BigDecimal.ZERO)) {
                            OutboundReferenceProductRequest productRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                productRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            productRequest.setProductId(currentOutboundReferenceProductRequest.getProductId());
                            productRequest.setUnit(currentOutboundReferenceProductRequest.getUnit());
                            productRequest.setQuantity(request.getOutboundProducts().get(i).getExportQuantity());
                            productRequest.setUnitPrice(currentOutboundReferenceProductRequest.getUnitPrice());
                            productRequest.setDiscount(currentOutboundReferenceProductRequest.getDiscount());
                            productRequest.setTax(currentOutboundReferenceProductRequest.getTax());
                            productRequest.setOriginalPrice(currentOutboundReferenceProductRequest.getUnitPrice().multiply(currentOutboundReferenceProductRequest.getQuantity()));
                            productRequest.setPrice(calcTotalPrice(productRequest.getUnitPrice(), productRequest.getQuantity(),
                                    productRequest.getDiscount(), productRequest.getTax()));
                            productRequest.setStatus(OutboundProductStatus.DA_XUAT.getCode());
                            productRequest.setOutboundProductId(currentOutboundReferenceProductRequest.getOutboundProductId());
                            productRequest.setOutboundTicketId(currentOutboundReferenceProductRequest.getOutboundTicketId());
                            currentNewOutboundReferenceProductRequests.add(productRequest);

                            currentNewOutboundReferenceProductRequests.remove(currentOutboundReferenceProductRequest);
                            // Thêm mới vật tư trong phiếu dịch vụ với số lượng bằng phần chưa xuất kho, trạng thái chưa xuất
                            OutboundReferenceProductRequest newOutboundReferenceProductRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                newOutboundReferenceProductRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            currentOutboundReferenceProductRequest.setProductId(currentOutboundReferenceProductRequest.getProductId());
                            currentOutboundReferenceProductRequest.setUnit(currentOutboundReferenceProductRequest.getUnit());
                            currentOutboundReferenceProductRequest.setQuantity(outboundProducts.get(i).getRequestQuantity().subtract(request.getOutboundProducts().get(i).getExportQuantity()));
                            currentOutboundReferenceProductRequest.setUnitPrice(currentOutboundReferenceProductRequest.getUnitPrice());
                            currentOutboundReferenceProductRequest.setDiscount(BigDecimal.ZERO);
                            currentOutboundReferenceProductRequest.setTax(currentOutboundReferenceProductRequest.getTax());
                            currentOutboundReferenceProductRequest.setOriginalPrice(currentOutboundReferenceProductRequest.getUnitPrice().multiply(currentOutboundReferenceProductRequest.getQuantity()));
                            currentOutboundReferenceProductRequest.setPrice(calcTotalPrice(currentOutboundReferenceProductRequest.getUnitPrice(), currentOutboundReferenceProductRequest.getQuantity(),
                                    currentOutboundReferenceProductRequest.getDiscount(), currentOutboundReferenceProductRequest.getTax()));
                            currentOutboundReferenceProductRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                            currentOutboundReferenceProductRequest.setOutboundProductId(outboundProduct.getId());
                            currentOutboundReferenceProductRequest.setOutboundTicketId(outboundProduct.getOutboundTicketId());
                            currentNewOutboundReferenceProductRequests.add(currentOutboundReferenceProductRequest);
                        } else {
                            OutboundReferenceProductRequest productRequest = new OutboundReferenceProductRequest();
                            if (Boolean.TRUE.equals(flagRequest.getIsSellSparePart())) {
                                productRequest.setSellSparePartId(outboundTicket.getTicketId());
                            }
                            productRequest.setProductId(currentOutboundReferenceProductRequest.getProductId());
                            productRequest.setUnit(currentOutboundReferenceProductRequest.getUnit());
                            productRequest.setQuantity(request.getOutboundProducts().get(i).getRequestQuantity());
                            productRequest.setUnitPrice(currentOutboundReferenceProductRequest.getUnitPrice());
                            productRequest.setDiscount(currentOutboundReferenceProductRequest.getDiscount());
                            productRequest.setTax(currentOutboundReferenceProductRequest.getTax());
                            productRequest.setOriginalPrice(currentOutboundReferenceProductRequest.getUnitPrice().multiply(currentOutboundReferenceProductRequest.getQuantity()));
                            productRequest.setPrice(calcTotalPrice(currentOutboundReferenceProductRequest.getUnitPrice(), currentOutboundReferenceProductRequest.getQuantity(),
                                    currentOutboundReferenceProductRequest.getDiscount(), currentOutboundReferenceProductRequest.getTax()));
                            productRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                            productRequest.setOutboundProductId(outboundProduct.getId());
                            productRequest.setOutboundTicketId(outboundProduct.getOutboundTicketId());
                            currentNewOutboundReferenceProductRequests.add(productRequest);

                            currentNewOutboundReferenceProductRequests.remove(currentOutboundReferenceProductRequest);
                        }
                    }
                }
            }

            // Nếu SL thực tế = SL yêu cầu -> Đổi trạng thái của sản phẩm này trong phiếu DV thành đã xuất kho
            if (outboundProducts.get(i).getRequestQuantity().compareTo(request.getOutboundProducts().get(i).getExportQuantity()) == 0) {
                for (OutboundReferenceProductRequest newOutboundReferenceProductRequest : newOutboundReferenceProductRequests) {
                    if (newOutboundReferenceProductRequest.getOutboundProductId().equals(outboundProducts.get(i).getId())) {
                        newOutboundReferenceProductRequest.setStatus(OutboundProductStatus.DA_XUAT.getCode());
                    }
                }
                for (OutboundReferenceProductRequest currentNewOutboundReferenceProductRequest : currentNewOutboundReferenceProductRequests) {
                    if (currentNewOutboundReferenceProductRequest.getOutboundProductId().equals(outboundProducts.get(i).getId())) {
                        currentNewOutboundReferenceProductRequest.setStatus(OutboundProductStatus.DA_XUAT.getCode());
                    }
                }
            }

            // Giảm số lượng tồn kho phụ tùng
            Optional<Product> optionalProduct = this.productRepository.findById(outboundProducts.get(i).getProductId());
            Product product = optionalProduct.orElse(new Product());
            product.setQuantity(product.getQuantity().subtract(request.getOutboundProducts().get(i).getExportQuantity()));
            // Lưu log
            int finalI = i;
            Thread thread = new Thread(() -> this.saveProductLog(actionUser, product, product.getQuantity().subtract(request.getOutboundProducts().get(finalI).getExportQuantity())));
            thread.start();

            if (request.getOutboundProducts().get(i).getExportQuantity().compareTo(BigDecimal.ZERO) > 0 && (product.getFirstSellAt() == null)) {
                    product.setFirstSellAt(new Date());
            }
            this.productRepository.save(product);
        }
    }

    private static BigDecimal calcTotalPrice(BigDecimal unitPrice, BigDecimal quantity, BigDecimal discount, BigDecimal tax) {
        BigDecimal originPrice = unitPrice.multiply(quantity);
        return originPrice.subtract(discount).multiply(BigDecimal.ONE.add(tax));
    }

    private void saveProductLog(String actionUser, Product product, BigDecimal quantity) {
        log.info("============ start save log product ==========");
        Gson gson = new Gson();
        Product newProduct = gson.fromJson(gson.toJson(product), Product.class);
        newProduct.setQuantity(quantity);
        ProductLog productLog = new ProductLog();
        productLog.setProductId(product.getId());
        productLog.setActionUser(actionUser);
        productLog.setOldValue(new Gson().toJson(product));
        productLog.setNewValue(new Gson().toJson(newProduct));
        this.productLogRepository.save(productLog);
    }
}
