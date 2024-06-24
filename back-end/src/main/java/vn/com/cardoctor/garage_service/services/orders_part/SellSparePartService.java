package vn.com.cardoctor.garage_service.services.orders_part;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.orders_part.LogSellSparePart;
import vn.com.cardoctor.garage_service.entities.orders_part.SellSparePart;
import vn.com.cardoctor.garage_service.entities.orders_part.SellSparePartProduct;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartFilter;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartProductRequest;
import vn.com.cardoctor.garage_service.models.requests.orders_part.SellSparePartRequest;
import vn.com.cardoctor.garage_service.models.responses.orders_part.SellSparePartProductResponse;
import vn.com.cardoctor.garage_service.models.responses.orders_part.SellSparePartResponse;
import vn.com.cardoctor.garage_service.repositories.CustomerRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.LogSellSparePartRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.SellSparePartProductRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.SellSparePartRepository;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.services.GeneratorCodeTicketService;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
public class SellSparePartService extends BaseService {
    @Autowired
    SellSparePartRepository sellSparePartRepository;

    @Autowired
    SellSparePartProductRepository sellSparePartProductRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OutboundTicketRepository outboundTicketRepository;

    @Autowired
    InboundTicketRepository inboundTicketRepository;

    @Autowired
    InboundProductRepository inboundProductRepository;

    @Autowired
    OutboundProductRepository outboundProductRepository;

    @Autowired
    LogSellSparePartRepository logSellSparePartRepository;

    @Autowired
    GeneratorCodeTicketService generatorCodeTicketService;

    public PagingDataResponse findAllSellSparePart(Long garageId, SellSparePartFilter sparePartFilter, Integer pageSize, Integer pageNumber) {
        return this.sellSparePartRepository.findAllSellSparePart(garageId, sparePartFilter, pageSize, pageNumber);
    }

    public SellSparePartResponse findById(Long sellSparePartId) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin đơn bán hàng");
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        SellSparePartResponse response = new SellSparePartResponse();
        response.setId(sellSparePart.getId());
        response.setSellCode(sellSparePart.getSellCode());
        response.setCustomerId(sellSparePart.getCustomerId());
        response.setDeliveryStatus(sellSparePart.getDeliveryStatus());
        response.setPaymentStatus(sellSparePart.getPaymentStatus());
        response.setDiscount(sellSparePart.getDiscount());
        response.setTax(sellSparePart.getTax());
        response.setTotalPrice(sellSparePart.getTotalPrice());
        response.setGarageId(sellSparePart.getGarageId());
        response.setInventoryId(sellSparePart.getInventoryId());
        response.setDiscountType(sellSparePart.getDiscountType());
        ObjectMapper mapper = new ObjectMapper();
        String jsonProducts = sellSparePart.getProducts();
        List<SellSparePartProductResponse> listProducts = Arrays.asList(mapper.readValue(jsonProducts, SellSparePartProductResponse[].class));
        response.setProducts(listProducts);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(SellSparePartRequest sellSparePartRequest, Long garageId, Integer discountType) throws ApiException, JsonProcessingException {
        this.validRequest(sellSparePartRequest);
        SellSparePart sellSparePart = new SellSparePart();
        if (sellSparePartRequest.getCustomerId() != null) {
            sellSparePart.setCustomerId(sellSparePartRequest.getCustomerId());
        } else {
            Customer customer = new Customer();
            customer.setFullName(sellSparePartRequest.getCustomerName());
            customer.setPhoneNumber(sellSparePartRequest.getCustomerPhone());
            customer.setCustomerTypeId(sellSparePartRequest.getCustomerTypeId());
            customer.setGarageId(garageId);
            customer = this.customerRepository.save(customer);
            sellSparePart.setCustomerId(customer.getId());
        }
        this.convertRequestToEntity(sellSparePartRequest, sellSparePart);
        sellSparePart.setDiscountType(discountType);
        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.RECEIVE_ORDER.getCode());
        sellSparePart.setPaymentStatus(PaymentCompletedStatus.WAIT_FOR_PAY.getCode());
        sellSparePart.setGarageId(garageId);
        String code = this.generatorCodeTicketService.generateTicketCode(garageId, "BH");
        sellSparePart.setSellCode(code);
        sellSparePart = this.sellSparePartRepository.save(sellSparePart);

        List<SellSparePartProduct> sellSparePartProducts = new ArrayList<>();
        BigDecimal discount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (SellSparePartProductRequest sellSparePartProductRequest : sellSparePartRequest.getProducts()) {
            if (sellSparePartProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Phụ tùng không tồn tại");
            }
            SellSparePartProduct product = new SellSparePartProduct();
            product.setSellSparePartId(sellSparePart.getId());
            product.setProductId(sellSparePartProductRequest.getProductId());
            product.setUnit(sellSparePartProductRequest.getUnit());
            product.setQuantity(sellSparePartProductRequest.getQuantity() == null ? BigDecimal.ZERO : sellSparePartProductRequest.getQuantity());
            product.setUnitPrice(sellSparePartProductRequest.getUnitPrice());
            product.setDiscount(sellSparePartProductRequest.getDiscount());
            product.setTax(sellSparePartProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            discount = sellSparePart.getDiscountType().equals(1) ? discount.add(sellSparePartProductRequest.getDiscount())
                    : discount.add(originPrice.multiply(sellSparePartProductRequest.getDiscount()));
            tax = tax.add(originPrice.multiply(product.getTax()));
            BigDecimal price = sellSparePart.getDiscountType().equals(1) ? originPrice.subtract(sellSparePartProductRequest.getDiscount()).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(sellSparePartProductRequest.getDiscount())).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            sellSparePartProducts.add(product);
        }


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(sellSparePartProducts);

        LogSellSparePart logSellSparePart = new LogSellSparePart();
        logSellSparePart.setSellSparePartId(sellSparePart.getId());
        logSellSparePart.setLogVersion(1);
        logSellSparePart.setProducts(jsonProducts);
        logSellSparePart.setActionUser(this.getKeyCloakUserId());
        this.logSellSparePartRepository.save(logSellSparePart);

        sellSparePart.setProducts(jsonProducts);
        sellSparePart.setDiscount(discount);
        sellSparePart.setTax(tax);
        sellSparePart.setTotalPrice(totalPrice);
        sellSparePart = this.sellSparePartRepository.save(sellSparePart);

        return sellSparePart.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createV2(SellSparePartRequest sellSparePartRequest, Long garageId, Integer discountType) throws ApiException, JsonProcessingException {
        this.validRequest(sellSparePartRequest);
        SellSparePart sellSparePart = new SellSparePart();
        if (sellSparePartRequest.getCustomerId() != null) {
            sellSparePart.setCustomerId(sellSparePartRequest.getCustomerId());
        } else {
            Customer customer = new Customer();
            customer.setFullName(sellSparePartRequest.getCustomerName());
            customer.setPhoneNumber(sellSparePartRequest.getCustomerPhone());
            customer.setCustomerTypeId(sellSparePartRequest.getCustomerTypeId());
            customer.setGarageId(garageId);
            customer = this.customerRepository.save(customer);
            sellSparePart.setCustomerId(customer.getId());
        }
        this.convertRequestToEntity(sellSparePartRequest, sellSparePart);
        sellSparePart.setDiscountType(discountType);
        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.RECEIVE_ORDER.getCode());
        sellSparePart.setPaymentStatus(PaymentCompletedStatus.WAIT_FOR_PAY.getCode());
        sellSparePart.setGarageId(garageId);
        String ticketCode = this.generatorCodeTicketService.generateTicketCode(garageId, "BH");
        sellSparePart.setSellCode(ticketCode);
        sellSparePart = this.sellSparePartRepository.save(sellSparePart);

        // Tạo phiếu xuất kho
        OutboundTicket outboundTicket = new OutboundTicket();
        String code = generatorCodeTicketService.generateCodeInOutBound(sellSparePart.getGarageId(), "BH", "XK", OutboundType.FOR_SELL_SPARE_PART.getCode());
        String outboundCode = code + "-01";
        outboundTicket.setCode(outboundCode);
        outboundTicket.setType(OutboundType.FOR_SELL_SPARE_PART.getCode());
        outboundTicket.setTicketId(sellSparePart.getId());
        outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
        outboundTicket.setGarageId(sellSparePart.getGarageId());
        outboundTicket = this.outboundTicketRepository.save(outboundTicket);

        List<SellSparePartProduct> sellSparePartProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        for (SellSparePartProductRequest sellSparePartProductRequest : sellSparePartRequest.getProducts()) {
            if (sellSparePartProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Phụ tùng không tồn tại");
            }
            SellSparePartProduct product = new SellSparePartProduct();
            product.setSellSparePartId(sellSparePart.getId());
            product.setProductId(sellSparePartProductRequest.getProductId());
            product.setUnit(sellSparePartProductRequest.getUnit());
            product.setQuantity(sellSparePartProductRequest.getQuantity() == null ? BigDecimal.ZERO : sellSparePartProductRequest.getQuantity());
            product.setUnitPrice(sellSparePartProductRequest.getUnitPrice());
            product.setDiscount(sellSparePartProductRequest.getDiscount());
            product.setTax(sellSparePartProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            BigDecimal price = sellSparePart.getDiscountType().equals(1) ? originPrice.subtract(sellSparePartProductRequest.getDiscount()).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(sellSparePartProductRequest.getDiscount())).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            sellSparePartProducts.add(product);
        }

        // Thêm phụ tùng vào phiếu xuất kho "outboundTicket"
        for (SellSparePartProduct sellSparePartProduct : sellSparePartProducts) {
            OutboundProduct outboundProduct = new OutboundProduct();
            outboundProduct.setOutboundTicketId(outboundTicket.getId());
            outboundProduct.setProductId(sellSparePartProduct.getProductId());
            outboundProduct.setUnit(sellSparePartProduct.getUnit());
            outboundProduct.setRequestQuantity(sellSparePartProduct.getQuantity());
            outboundProduct.setExportQuantity(BigDecimal.ZERO);
            outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
            outboundProduct = this.outboundProductRepository.save(outboundProduct);
            sellSparePartProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
            sellSparePartProduct.setOutboundProductId(outboundProduct.getId());
            sellSparePartProduct.setOutboundTicketId(outboundTicket.getId());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(sellSparePartProducts);

        this.saveLog(sellSparePart.getId(), jsonProducts);

        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.ORDER_SUCCESS.getCode());
        sellSparePart.setProducts(jsonProducts);
        sellSparePart.setTotalPrice(totalPrice);
        this.sellSparePartRepository.save(sellSparePart);
        return sellSparePart.getId();
    }

    public Long update(Long sellSparePartId, SellSparePartRequest sellSparePartRequest) throws ApiException, JsonProcessingException {
        this.validRequest(sellSparePartRequest);
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin đơn bán hàng");
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        sellSparePart.setCustomerId(sellSparePartRequest.getCustomerId());
        this.convertRequestToEntity(sellSparePartRequest, sellSparePart);
        sellSparePart.setGarageId(sellSparePart.getGarageId());
        List<SellSparePartProduct> sellSparePartProducts = new ArrayList<>();
        BigDecimal discount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (SellSparePartProductRequest sellSparePartProductRequest : sellSparePartRequest.getProducts()) {
            if (sellSparePartProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Phụ tùng không tồn tại");
            }
            SellSparePartProduct product = new SellSparePartProduct();
            product.setSellSparePartId(sellSparePart.getId());
            product.setProductId(sellSparePartProductRequest.getProductId());
            product.setUnit(sellSparePartProductRequest.getUnit());
            product.setQuantity(sellSparePartProductRequest.getQuantity() == null ? BigDecimal.ZERO : sellSparePartProductRequest.getQuantity());
            product.setUnitPrice(sellSparePartProductRequest.getUnitPrice());
            product.setDiscount(sellSparePartProductRequest.getDiscount());
            product.setTax(sellSparePartProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            discount = sellSparePart.getDiscountType().equals(1) ? discount.add(sellSparePartProductRequest.getDiscount())
                    : discount.add(originPrice.multiply(sellSparePartProductRequest.getDiscount()));
            tax = tax.add(originPrice.multiply(product.getTax()));
            BigDecimal price = sellSparePart.getDiscountType().equals(1) ? originPrice.subtract(sellSparePartProductRequest.getDiscount()).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(sellSparePartProductRequest.getDiscount())).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            sellSparePartProducts.add(product);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(sellSparePartProducts);

        this.saveLog(sellSparePartId, jsonProducts);

        sellSparePart.setProducts(jsonProducts);
        sellSparePart.setDiscount(discount);
        sellSparePart.setTax(tax);
        sellSparePart.setTotalPrice(totalPrice);
        sellSparePart = this.sellSparePartRepository.save(sellSparePart);

        return sellSparePart.getId();
    }

    public Long updateHandlingTicketSellSparePart(Long sellSparePartId, SellSparePartRequest request) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        List<SellSparePartProductRequest> sellSparePartProducts = new ObjectMapper().readValue(sellSparePart.getProducts(), new TypeReference<List<SellSparePartProductRequest>>(){});
        // Tạo mới phiếu xuất kho nếu có thêm phụ tùng
        if (!request.getAddProducts().isEmpty()) {
            OutboundTicket outboundTicket = new OutboundTicket();
            String code = sellSparePart.getSellCode();
            String[] splitedCode = code.split("BH");
            String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_SELL_SPARE_PART.getCode() + splitedCode[1];
            int numberOfOutbound = this.outboundTicketRepository.countOutboundTicketByTicketIdAndType(sellSparePartId, OutboundType.FOR_SELL_SPARE_PART.getCode()) + 1;
            outboundCode = outboundCode + "-" + String.format("%02d", numberOfOutbound);
            outboundTicket.setCode(outboundCode);
            outboundTicket.setType(OutboundType.FOR_SELL_SPARE_PART.getCode());
            outboundTicket.setTicketId(sellSparePartId);
            outboundTicket.setGarageId(sellSparePart.getGarageId());
            outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
            outboundTicket = this.outboundTicketRepository.save(outboundTicket);
            for (SellSparePartProductRequest sellSparePartRequest : request.getAddProducts()) {
                OutboundProduct outboundProduct = new OutboundProduct();
                outboundProduct.setOutboundTicketId(outboundTicket.getId());
                outboundProduct.setProductId(sellSparePartRequest.getProductId());
                outboundProduct.setUnit(sellSparePartRequest.getUnit());
                outboundProduct.setRequestQuantity(sellSparePartRequest.getQuantity());
                outboundProduct.setExportQuantity(BigDecimal.ZERO);
                outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                // Save thông tin sản phẩm xuất kho -> Thêm vào log của phụ tùng trong phiếu báo giá
                outboundProduct = this.outboundProductRepository.save(outboundProduct);
                sellSparePartRequest.setOutboundProductId(outboundProduct.getId());
                sellSparePartProducts.add(sellSparePartRequest);
                log.info("size is {} line 420 is {}", sellSparePartProducts.size(), sellSparePartProducts.toString());
            }
        }


        // Xử lí nếu xoá sản phẩm
        // Nếu sản phẩm đã xuất kho -> Tạo phiếu nhập
        // Nếu sản phẩm chưa xuất kho -> Ẩn phiếu xuất kho chứa sản phẩm đó, tạo phiếu xuất kho mới với các sản phẩm còn lại
        if (!request.getRemoveProducts().isEmpty()) {
            InboundTicket inboundTicketForOutbounded; // Phiếu nhập kho cho sản phẩm đã xuất kho
            List<InboundProduct> inboundProductsForOutbounded = new ArrayList<>(); // List sản phẩm thuộc phiếu nhập kho trên

            OutboundTicket outboundTicketForNotOutbounded = new OutboundTicket(); // Phiếu xuất kho cho sản phẩm còn lại chưa xuất kho
            List<OutboundProduct> outboundProductsForNotOutbounded = new ArrayList<>();; // List sản phẩm thuộc phiếu xuất kho trên
            Set<Long> outboundTicketIdsDeleted = new HashSet<>(); // List id của các phiếu xuất kho chứa sản phẩm bị xoá gắn với phiếu dịch vụ
            List<OutboundProduct> outboundProductsInTicketDeleted = new ArrayList<>(); // List sản phẩm thuộc các phiếu trên

            for (SellSparePartProductRequest sellSparePartRequest : request.getRemoveProducts()) {
                // Nếu sản phẩm đã xuất kho -> Tạo phiếu nhập
                if (sellSparePartRequest.getStatus().equals(OutboundProductStatus.DA_XUAT.getCode())) {
                    // Nếu sản phẩm đã xuất kho -> Thêm vào list inboundProductForOutbounded
                    InboundProduct inboundProduct = new InboundProduct();
                    inboundProduct.setProductId(sellSparePartRequest.getProductId());
                    inboundProduct.setUnit(sellSparePartRequest.getUnit());
                    inboundProduct.setDistributorId(0L);
                    inboundProduct.setRequestQuantity(sellSparePartRequest.getQuantity());
                    inboundProduct.setExportQuantity(BigDecimal.ZERO);
                    inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                    inboundProductsForOutbounded.add(inboundProduct);
                    // Xoá sản phẩm đó khỏi log của phụ tùng trong phiếu báo giá
                    sellSparePartProducts.removeIf(sellSparePartProduct ->
                            sellSparePartProduct.getOutboundProductId().equals(sellSparePartRequest.getOutboundProductId())
                    );
                }

                // Nếu sản phẩm chưa xuất kho -> Xoá phiếu xuất kho chứa sản phẩm đó
                // Sau đó lưu các sản phẩm còn lại vào list inboundProductForNotOutbounded
                // Tạo phiếu xuất kho mới với các sản phẩm trong list inboundProductForNotOutbounded
                if (sellSparePartRequest.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                    // Đánh dấu phiếu xuất kho đã bị xoá
                    Optional<OutboundProduct> oOutboundProductNotOutbounded = this.outboundProductRepository.findById(sellSparePartRequest.getOutboundProductId());
                    OutboundProduct outboundProductNotOutbounded = oOutboundProductNotOutbounded.get();
                    outboundTicketIdsDeleted.add(outboundProductNotOutbounded.getOutboundTicketId());
                }
            }

            if (!inboundProductsForOutbounded.isEmpty()) {
                inboundTicketForOutbounded = new InboundTicket();
                String code = sellSparePart.getSellCode();
                String[] splitedCode = code.split("BH");
                String inboundCode = splitedCode[0] + "NK" + InboundType.REFUND_SELL_TICKET.getCode() + splitedCode[1];
                int numberOfInbound = this.inboundTicketRepository.countInboundTicketByTicketIdAndType(sellSparePartId, InboundType.REFUND_SELL_TICKET.getCode()) + 1;
                inboundCode = inboundCode + "-" + String.format("%02d", numberOfInbound);
                inboundTicketForOutbounded.setCode(inboundCode);
                inboundTicketForOutbounded.setType(InboundType.REFUND_SELL_TICKET.getCode());
                inboundTicketForOutbounded.setTicketId(sellSparePartId);
                inboundTicketForOutbounded.setGarageId(sellSparePart.getGarageId());
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
            });

            // Remove những sản phẩm chưa xuất kho ở danh sách sản phẩm thuộc các phiếu outboundTicketIdsDeleted
            // Xoá những sản phẩm ở chưa xuất kho phiếu dịch vụ
            for (SellSparePartProductRequest sellSparePartRequest : request.getRemoveProducts()) {
                if (sellSparePartRequest.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                    outboundProductsInTicketDeleted.removeIf(product ->
                            product.getId().equals(sellSparePartRequest.getOutboundProductId())
                    );
                    sellSparePartProducts.removeIf(sellSparePartProduct ->
                            sellSparePartProduct.getOutboundProductId().equals(sellSparePartRequest.getOutboundProductId())
                    );
                }
            }

            // Tạo mới các sản phẩm thuộc phiếu xuất kho outboundTicketForNotOutbounded
            if (!outboundProductsInTicketDeleted.isEmpty()) {
                String code = sellSparePart.getSellCode();
                String[] splitedCode = code.split("BH");
                String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_SELL_SPARE_PART.getCode() + splitedCode[1];
                int numberOfOutbound = this.outboundTicketRepository.countOutboundTicketByTicketIdAndType(sellSparePartId, OutboundType.FOR_SELL_SPARE_PART.getCode()) + 1;
                outboundCode = outboundCode + "-" + String.format("%02d", numberOfOutbound);

                outboundTicketForNotOutbounded.setCode(outboundCode);
                outboundTicketForNotOutbounded.setType(OutboundType.FOR_SELL_SPARE_PART.getCode());
                outboundTicketForNotOutbounded.setTicketId(sellSparePartId);
                outboundTicketForNotOutbounded.setGarageId(sellSparePart.getGarageId());
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
                    Optional<SellSparePartProductRequest> oSellSparePartProductRequest = sellSparePartProducts
                            .stream()
                            .filter(sparePartInRepairOrder -> sparePartInRepairOrder.getOutboundProductId().equals(outboundProductInTicketDeleted.getId()))
                            .findAny();
                    if (oSellSparePartProductRequest.isPresent()) {
                        SellSparePartProductRequest sellSparePartProductRequest = oSellSparePartProductRequest.get();
                        sellSparePartProductRequest.setOutboundProductId(outboundProduct.getId());
                        // Set lại báo giá phụ tùng
                    }
                }
            }
        }

        List<SellSparePartProduct> finalSellSparePartProducts = new ArrayList<>();
        BigDecimal discount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (SellSparePartProductRequest sellSparePartProductRequest : sellSparePartProducts) {
            if (sellSparePartProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Phụ tùng không tồn tại");
            }
            SellSparePartProduct product = new SellSparePartProduct();
            product.setSellSparePartId(sellSparePart.getId());
            product.setProductId(sellSparePartProductRequest.getProductId());
            product.setUnit(sellSparePartProductRequest.getUnit());
            product.setQuantity(sellSparePartProductRequest.getQuantity() == null ? BigDecimal.ZERO : sellSparePartProductRequest.getQuantity());
            product.setUnitPrice(sellSparePartProductRequest.getUnitPrice());
            product.setDiscount(sellSparePartProductRequest.getDiscount());
            product.setTax(sellSparePartProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            discount = sellSparePart.getDiscountType().equals(1) ? discount.add(sellSparePartProductRequest.getDiscount())
                    : discount.add(originPrice.multiply(sellSparePartProductRequest.getDiscount()));
            tax = tax.add(originPrice.multiply(product.getTax()));
            BigDecimal price = sellSparePart.getDiscountType().equals(1) ? originPrice.subtract(sellSparePartProductRequest.getDiscount()).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(sellSparePartProductRequest.getDiscount())).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            product.setStatus(sellSparePartProductRequest.getStatus());
            product.setOutboundProductId(sellSparePartProductRequest.getOutboundProductId());
            totalPrice = totalPrice.add(price);
            finalSellSparePartProducts.add(product);
        }

        log.info("final sellSparePartProducts is {}", finalSellSparePartProducts.toString());


        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(finalSellSparePartProducts);

        this.saveLog(sellSparePartId, jsonProducts);

        sellSparePart.setProducts(jsonProducts);
        sellSparePart.setDiscount(discount);
        sellSparePart.setTax(tax);
        sellSparePart.setTotalPrice(totalPrice);
        this.sellSparePartRepository.save(sellSparePart);

        return sellSparePartId;
    }

    public Long handleTicketSellSparePart(Long sellSparePartId, SellSparePartRequest sellSparePartRequest) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        // Tạo phiếu xuất kho
        OutboundTicket outboundTicket = new OutboundTicket();
        String code = generatorCodeTicketService.generateCodeInOutBound(sellSparePart.getGarageId(), "BH", "XK", OutboundType.FOR_SELL_SPARE_PART.getCode());
        String outboundCode = code + "-01";
        outboundTicket.setCode(outboundCode);
        outboundTicket.setType(OutboundType.FOR_SELL_SPARE_PART.getCode());
        outboundTicket.setTicketId(sellSparePart.getId());
        outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
        outboundTicket.setGarageId(sellSparePart.getGarageId());
        outboundTicket = this.outboundTicketRepository.save(outboundTicket);

        List<SellSparePartProduct> sellSparePartProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        for (SellSparePartProductRequest sellSparePartProductRequest : sellSparePartRequest.getProducts()) {
            if (sellSparePartProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Phụ tùng không tồn tại");
            }
            SellSparePartProduct product = new SellSparePartProduct();
            product.setSellSparePartId(sellSparePart.getId());
            product.setProductId(sellSparePartProductRequest.getProductId());
            product.setUnit(sellSparePartProductRequest.getUnit());
            product.setQuantity(sellSparePartProductRequest.getQuantity() == null ? BigDecimal.ZERO : sellSparePartProductRequest.getQuantity());
            product.setUnitPrice(sellSparePartProductRequest.getUnitPrice());
            product.setDiscount(sellSparePartProductRequest.getDiscount());
            product.setTax(sellSparePartProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            BigDecimal price = sellSparePart.getDiscountType().equals(1) ? originPrice.subtract(sellSparePartProductRequest.getDiscount()).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(sellSparePartProductRequest.getDiscount())).multiply(new BigDecimal(1).add(sellSparePartProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            sellSparePartProducts.add(product);
        }

        // Thêm phụ tùng vào phiếu xuất kho "outboundTicket"
        for (SellSparePartProduct sellSparePartProduct : sellSparePartProducts) {
            OutboundProduct outboundProduct = new OutboundProduct();
            outboundProduct.setOutboundTicketId(outboundTicket.getId());
            outboundProduct.setProductId(sellSparePartProduct.getProductId());
            outboundProduct.setUnit(sellSparePartProduct.getUnit());
            outboundProduct.setRequestQuantity(sellSparePartProduct.getQuantity());
            outboundProduct.setExportQuantity(BigDecimal.ZERO);
            outboundProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
            outboundProduct = this.outboundProductRepository.save(outboundProduct);
            sellSparePartProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
            sellSparePartProduct.setOutboundProductId(outboundProduct.getId());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(sellSparePartProducts);

        this.saveLog(sellSparePartId, jsonProducts);

        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.ORDER_SUCCESS.getCode());
        sellSparePart.setProducts(jsonProducts);
        sellSparePart.setTotalPrice(totalPrice);
        this.sellSparePartRepository.save(sellSparePart);
        return sellSparePartId;
    }

    public Long cancelTicketSellSparePart(Long sellSparePartId) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();

        if (sellSparePart.getDeliveryStatus().equals(DeliverySparePartStatus.RECEIVE_ORDER.getCode())) {
            sellSparePart.setDeliveryStatus(DeliverySparePartStatus.CANCELLED.getCode());
            sellSparePartRepository.save(sellSparePart);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            String jsonProducts = sellSparePart.getProducts();
            SellSparePartProduct[] listProducts = mapper.readValue(jsonProducts, SellSparePartProduct[].class);
            List<InboundProduct> inboundProducts = new ArrayList<>();
            for (SellSparePartProduct sellSparePartProduct : listProducts) {
                // Nếu phụ tùng chưa xuất kho -> Huỷ phiếu
                if (sellSparePartProduct.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                    Optional<OutboundProduct> oOutboundProduct = this.outboundProductRepository.findById(sellSparePartProduct.getOutboundProductId());
                    OutboundProduct outboundProduct = oOutboundProduct.orElse(new OutboundProduct());
                    Optional<OutboundTicket> oOutboundTicket = this.outboundTicketRepository.findById(outboundProduct.getOutboundTicketId());
                    OutboundTicket outboundTicket = oOutboundTicket.orElse(new OutboundTicket());
                    outboundTicket.setStatus(InOutboundStatus.DELETED.getCode());
                    this.outboundTicketRepository.save(outboundTicket);
                }
                // Nếu phụ tùng đã xuất kho -> Tạo phiếu nhập kho
                // Tạo thành 1 list sản phẩm để nhập kho
                if (sellSparePartProduct.getStatus().equals(OutboundProductStatus.DA_XUAT.getCode())) {
                    InboundProduct inboundProduct = new InboundProduct();
                    inboundProduct.setProductId(sellSparePartProduct.getProductId());
                    inboundProduct.setUnit(sellSparePartProduct.getUnit());
                    inboundProduct.setRequestQuantity(sellSparePartProduct.getQuantity());
                    inboundProduct.setExportQuantity(BigDecimal.ZERO);
                    inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                    inboundProducts.add(inboundProduct);
                }
            }
            // Nếu có sản phẩm đã xuất kho -> Tạo phiếu nhập kho
            if (!inboundProducts.isEmpty()) {
                InboundTicket inboundTicket = new InboundTicket();
                String code = sellSparePart.getSellCode();
                String[] splitedCode = code.split("BH");
                String inboundCode = splitedCode[0] + "NK" + InboundType.REFUND_SELL_TICKET.getCode() + splitedCode[1];
                int numberOfInbound = this.inboundTicketRepository.countInboundTicketByTicketIdAndType(sellSparePartId, InboundType.REFUND_SELL_TICKET.getCode()) + 1;
                inboundCode = inboundCode + "-" + String.format("%02d", numberOfInbound);
                inboundTicket.setCode(inboundCode);
                inboundTicket.setType(InboundType.REFUND_SELL_TICKET.getCode());
                inboundTicket.setTicketId(sellSparePartId);
                inboundTicket.setGarageId(sellSparePart.getGarageId());
                inboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
                inboundTicket = this.inboundTicketRepository.save(inboundTicket);
                for (InboundProduct inboundProduct : inboundProducts) {
                    inboundProduct.setInboundTicketId(inboundTicket.getId());
                }
                this.inboundProductRepository.saveAll(inboundProducts);
            }
            sellSparePart.setDeliveryStatus(DeliverySparePartStatus.CANCELLED.getCode());
            sellSparePartRepository.save(sellSparePart);
        }
        return sellSparePartId;
    }

    public Long deliveryTicketSellSparePart(Long sellSparePartId) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.ON_THE_WAY.getCode());

        // Check outbound all product
        ObjectMapper mapper = new ObjectMapper();
        String jsonProducts = sellSparePart.getProducts();
        SellSparePartProduct[] listProducts = mapper.readValue(jsonProducts, SellSparePartProduct[].class);
        for (SellSparePartProduct sellSparePartProduct : listProducts) {
            if (sellSparePartProduct.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                throw new ApiException(ERROR.BAD_REQUEST, "Còn tồn tại sản phẩm chưa được xuất kho");
            }
        }
        sellSparePartRepository.save(sellSparePart);
        return sellSparePartId;
    }

    public Long confirmReceiveTicketSellSparePart(Long sellSparePartId) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        // Check outbound all product
        ObjectMapper mapper = new ObjectMapper();
        String jsonProducts = sellSparePart.getProducts();
        SellSparePartProduct[] listProducts = mapper.readValue(jsonProducts, SellSparePartProduct[].class);
        for (SellSparePartProduct sellSparePartProduct : listProducts) {
            if (sellSparePartProduct.getStatus().equals(OutboundProductStatus.CHUA_XUAT.getCode())) {
                throw new ApiException(ERROR.BAD_REQUEST, "Còn tồn tại sản phẩm chưa được xuất kho");
            }
        }
        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.RECEIVE_PRODUCT.getCode());
        sellSparePartRepository.save(sellSparePart);
        return sellSparePartId;
    }

    public Long refundTicketSellSparePart(Long sellSparePartId) throws ApiException, JsonProcessingException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        SellSparePart sellSparePart = oSellSparePart.get();
        sellSparePart.setDeliveryStatus(DeliverySparePartStatus.REFUNDED.getCode());

        // Tạo phiếu nhập kho cho các sản phẩm
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, BigDecimal> productToInbound = new HashMap<>();
        String jsonProducts = sellSparePart.getProducts();
        SellSparePartProduct[] listProducts = mapper.readValue(jsonProducts, SellSparePartProduct[].class);
        for (SellSparePartProduct sellSparePartProduct : listProducts) {
            productToInbound.merge(sellSparePartProduct.getProductId(), sellSparePartProduct.getQuantity(), BigDecimal::add);
        }
        InboundTicket inboundTicket = new InboundTicket();
        String code = sellSparePart.getSellCode();
        String[] splitedCode = code.split("BH");
        String inboundCode = splitedCode[0] + "NK" + InboundType.REFUND_SELL_TICKET.getCode() + splitedCode[1];
        int numberOfInbound = this.inboundTicketRepository.countInboundTicketByTicketIdAndType(sellSparePartId, InboundType.REFUND_SELL_TICKET.getCode()) + 1;
        inboundCode = inboundCode + "-" + String.format("%02d", numberOfInbound);
        inboundTicket.setCode(inboundCode);
        inboundTicket.setType(InboundType.REFUND_SELL_TICKET.getCode());
        inboundTicket.setTicketId(sellSparePartId);
        inboundTicket.setGarageId(sellSparePart.getGarageId());
        inboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
        inboundTicket = this.inboundTicketRepository.save(inboundTicket);

        for (Map.Entry<Long, BigDecimal> entry : productToInbound.entrySet()) {
            InboundProduct inboundProduct = new InboundProduct();
            inboundProduct.setProductId(entry.getKey());
            Product product = this.productRepository.findById(entry.getKey()).orElse(null);
            inboundProduct.setUnit((product != null && product.getUnit() != null) ? product.getUnit() : null);
            inboundProduct.setRequestQuantity(entry.getValue());
            inboundProduct.setExportQuantity(BigDecimal.ZERO);
            inboundProduct.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
            inboundProduct.setInboundTicketId(inboundTicket.getId());
            this.inboundProductRepository.save(inboundProduct);
        }
        return sellSparePartId;
    }


    public Long changePaymentStatus(Long sellSparePartId, int paymentCompleteStatus) throws ApiException {
        Optional<SellSparePart> oSellSparePart = this.sellSparePartRepository.findById(sellSparePartId);
        if (oSellSparePart.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        validPaymentStatus(paymentCompleteStatus);
        SellSparePart sellSparePart = oSellSparePart.get();
        sellSparePart.setPaymentStatus(paymentCompleteStatus);
        sellSparePartRepository.save(sellSparePart);
        return sellSparePartId;
    }

    public void convertRequestToEntity(SellSparePartRequest sellSparePartRequest, SellSparePart sellSparePart) {
        sellSparePart.setDiscount(sellSparePartRequest.getDiscount());
        sellSparePart.setTax(sellSparePartRequest.getTax());
    }

    public void validRequest(SellSparePartRequest sellSparePartRequest) throws ApiException {
        if (Objects.isNull(sellSparePartRequest.getCustomerId()) && Objects.isNull(sellSparePartRequest.getCustomerPhone())) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Thiếu thông tin khách hàng");
        }
        if (sellSparePartRequest.getProducts().isEmpty()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Đơn bán phải tồn tại thông tin phụ tùng");
        }
    }

    public void validPaymentStatus(int paymentStatus) throws ApiException {
        PaymentCompletedStatus paymentCompletedStatus = PaymentCompletedStatus.getPaymentCompletedStatus(paymentStatus);
        if (paymentCompletedStatus == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin trạng thái thanh toán không đúng");
        }
    }

    private void saveLog(Long sellSparePartId, String newJsonProducts) {
        LogSellSparePart logSellSparePart;
        Optional<LogSellSparePart> oLogSellSparePart = this.logSellSparePartRepository.findFirstBySellSparePartIdOrderByLogVersionDesc(sellSparePartId);
        if (oLogSellSparePart.isPresent()) {
            logSellSparePart = oLogSellSparePart.get();
            LogSellSparePart newLogSellSparePart = new LogSellSparePart();
            newLogSellSparePart.setSellSparePartId(sellSparePartId);
            newLogSellSparePart.setLogVersion(logSellSparePart.getLogVersion() + 1);
            newLogSellSparePart.setProducts(newJsonProducts);
            newLogSellSparePart.setActionUser(this.getKeyCloakUserId());
            this.logSellSparePartRepository.save(newLogSellSparePart);
        }
    }
}
