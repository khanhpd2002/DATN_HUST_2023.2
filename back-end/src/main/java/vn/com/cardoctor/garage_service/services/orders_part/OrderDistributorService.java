package vn.com.cardoctor.garage_service.services.orders_part;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.PagingDataResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.orders_part.LogOrderDistributor;
import vn.com.cardoctor.garage_service.entities.orders_part.OrderDistributor;
import vn.com.cardoctor.garage_service.entities.orders_part.OrderDistributorProduct;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.events.EventPublisher;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorProductRequest;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorRequest;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.models.responses.orders_part.OrderDistributorProductResponse;
import vn.com.cardoctor.garage_service.models.responses.orders_part.OrderDistributorResponse;
import vn.com.cardoctor.garage_service.repositories.DistributorRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.OutboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.LogOrderDistributorRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.OrderDistributorRepository;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.services.GeneratorCodeTicketService;
import vn.com.cardoctor.garage_service.services.ProductService;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderDistributorService extends BaseService {
    private final OrderDistributorRepository orderDistributorRepository;

    private final InventoryRepository inventoryRepository;

    private final DistributorRepository distributorRepository;

    private final InboundTicketRepository inboundTicketRepository;

    private final InboundProductRepository inboundProductRepository;

    private final OutboundTicketRepository outboundTicketRepository;

    private final OutboundProductRepository outboundProductRepository;

    private final LogOrderDistributorRepository logOrderDistributorRepository;

    private final GeneratorCodeTicketService generatorCodeTicketService;

    private final ProductService productService;

    private final EventPublisher eventPublisher;

    private static final String CODE_NAME_PRODUCT_PATTERN = "([^}]*)\\s-\\s([^}]*)";

    private static final String PRODUCT_NOT_EXIST = "Phụ tùng không tồn tại";

    public PagingDataResponse findAllOrderDistributor(Long garageId, String orderCode, Long distributorId, String distributorCode, String distributorName, Date fromDate, Date toDate,
                                                      Integer deliveryStatus, Integer paymentStatus, Integer pageSize, Integer pageNumber) {
        return this.orderDistributorRepository.findAllOrderDistributor(garageId, orderCode, distributorId, distributorCode, distributorName, fromDate, toDate,
                deliveryStatus, paymentStatus, pageSize, pageNumber);
    }

    public OrderDistributorResponse findById(Long orderDistributorId) throws ApiException, JsonProcessingException {
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin đơn hàng");
        }
        OrderDistributor orderDistributor = oOrderDistributor.get();
        OrderDistributorResponse response = new OrderDistributorResponse();
        response.setId(orderDistributor.getId());
        response.setOrderCode(orderDistributor.getOrderCode());
        response.setDistributorId(orderDistributor.getDistributorId());
        response.setDeliveryType(orderDistributor.getDeliveryType());
        response.setDeliveryStatus(orderDistributor.getDeliveryStatus());
        response.setPaymentStatus(orderDistributor.getPaymentStatus());
        response.setDiscount(orderDistributor.getDiscount());
        response.setTax(orderDistributor.getTax());
        response.setTotalPrice(orderDistributor.getTotalPrice());
        response.setGarageId(orderDistributor.getGarageId());
        response.setInventoryId(orderDistributor.getInventoryId());
        response.setDiscountType(orderDistributor.getDiscountType());
        ObjectMapper mapper = new ObjectMapper();
        String jsonProducts = orderDistributor.getProducts();
        List<OrderDistributorProductResponse> listProducts = Arrays.asList(mapper.readValue(jsonProducts, OrderDistributorProductResponse[].class));
        response.setProducts(listProducts);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(OrderDistributorRequest orderDistributorRequest, Long garageId, Integer discountType) throws ApiException, JsonProcessingException {
        this.validRequest(orderDistributorRequest);
        OrderDistributor orderDistributor = new OrderDistributor();
        this.convertRequestToEntity(orderDistributorRequest, orderDistributor);
        orderDistributor.setGarageId(garageId);
        orderDistributor.setDeliveryStatus(DeliverySparePartStatus.RECEIVE_ORDER.getCode());
        orderDistributor.setPaymentStatus(PaymentCompletedStatus.WAIT_FOR_PAY.getCode());
        String code = this.generatorCodeTicketService.generateTicketCode(garageId, "MH");
        orderDistributor.setOrderCode(code);

        if (Objects.nonNull(orderDistributorRequest.getDistributorId())) {
            orderDistributor.setDistributorId(orderDistributorRequest.getDistributorId());
        } else {
            Distributor distributor = new Distributor();
            distributor.setCode(orderDistributorRequest.getDistributorCode());
            distributor.setName(orderDistributorRequest.getDistributorName());
            distributor.setContactPhone(orderDistributorRequest.getDistributorPhone());
            distributor.setGarageId(garageId);
            distributor.setIsDelete(0);
            distributor = this.distributorRepository.save(distributor);
            orderDistributor.setDistributorId(distributor.getId());
        }
        orderDistributor.setDiscountType(discountType);
        orderDistributor = this.orderDistributorRepository.save(orderDistributor);

        List<OrderDistributorProduct> orderDistributorProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);
        BigDecimal discount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);

        for (OrderDistributorProductRequest orderDistributorProductRequest : orderDistributorRequest.getProducts()) {
            if (orderDistributorProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, PRODUCT_NOT_EXIST);
            }
            OrderDistributorProduct product = new OrderDistributorProduct();
            product.setOrderDistributorId(orderDistributor.getId());

            if (orderDistributorProductRequest.getProductId() != null) {
                product.setProductId(orderDistributorProductRequest.getProductId());
            } else {
                ProductRequest productRequest = new ProductRequest();
                String productCodeName = orderDistributorProductRequest.getProductName();
                // Regex cho định dạng công việc
                if (!Pattern.compile(CODE_NAME_PRODUCT_PATTERN).matcher(productCodeName).matches()) {
                    throw new ApiException(ERROR.INVALID_REQUEST, "Vui lòng nhập đúng định dạng phụ tùng");
                } else {
                    String[] splited = productCodeName.split("-");
                    String productCode = splited[0];
                    String productName = splited[1];
                    productRequest.setCode(productCode);
                    productRequest.setName(productName);
                    productRequest.setUnit(orderDistributorProductRequest.getUnit());
                    productRequest.setDistributorId(orderDistributor.getDistributorId());
                    productRequest.setPurchasePrice(orderDistributorProductRequest.getUnitPrice());
                    Optional<Inventory> oInventory = this.inventoryRepository.findFirstByGarageId(garageId);
                    if (oInventory.isEmpty()) {
                        throw new ApiException(ERROR.INVALID_REQUEST, "Garage chưa có kho, vui lòng liên hệ để được tạo");
                    }
                    Long productIdCreated = this.productService.create(productRequest, oInventory.get().getId()).getParentProductId();
                    product.setProductId(productIdCreated);
                }
            }
            product.setUnit(orderDistributorProductRequest.getUnit());
            product.setQuantity(orderDistributorProductRequest.getQuantity() == null ? BigDecimal.ZERO : orderDistributorProductRequest.getQuantity());
            product.setUnitPrice(orderDistributorProductRequest.getUnitPrice());
            product.setDiscount(orderDistributorProductRequest.getDiscount());
            product.setTax(orderDistributorProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            discount = orderDistributor.getDiscountType().equals(1) ? discount.add(orderDistributorProductRequest.getDiscount())
                    : discount.add(originPrice.multiply(orderDistributorProductRequest.getDiscount()));
            tax = tax.add(originPrice.multiply(product.getTax()));
            BigDecimal price = orderDistributor.getDiscountType().equals(1) ? originPrice.subtract(orderDistributorProductRequest.getDiscount()).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(orderDistributorProductRequest.getDiscount())).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            orderDistributorProducts.add(product);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(orderDistributorProducts);

        LogOrderDistributor logOrderDistributor = new LogOrderDistributor();
        logOrderDistributor.setOrderDistributorId(orderDistributor.getId());
        logOrderDistributor.setLogVersion(1);
        logOrderDistributor.setProducts(jsonProducts);
        logOrderDistributor.setActionUser(this.getKeyCloakUserId());
        this.logOrderDistributorRepository.save(logOrderDistributor);

        orderDistributor.setProducts(jsonProducts);
        orderDistributor.setDiscount(discount);
        orderDistributor.setTax(tax);
        orderDistributor.setTotalPrice(totalPrice);
        orderDistributor = this.orderDistributorRepository.save(orderDistributor);
        this.eventPublisher.publishEvent("create_order_distributor", orderDistributorRequest);
        return orderDistributor.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(Long orderDistributorId, OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        this.updateTicketOrderDistributor(orderDistributorRequest, orderDistributorId, null);
        return orderDistributorId;
    }

    public Long handleTicketOrderDistributor(Long orderDistributorId, OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        this.updateTicketOrderDistributor(orderDistributorRequest, orderDistributorId, DeliverySparePartStatus.ORDER_SUCCESS.getCode());
        return orderDistributorId;
    }

    public Long changePaymentStatus(Long orderDistributorId, int paymentCompleteStatus) throws ApiException {
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        validPaymentStatus(paymentCompleteStatus);
        OrderDistributor orderDistributor = oOrderDistributor.get();
        orderDistributor.setPaymentStatus(paymentCompleteStatus);
        this.orderDistributorRepository.save(orderDistributor);
        return orderDistributorId;
    }

    public Long deliveryTicketOrderDistributor(Long orderDistributorId, OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        this.updateTicketOrderDistributor(orderDistributorRequest, orderDistributorId, DeliverySparePartStatus.ON_THE_WAY.getCode());
        return orderDistributorId;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long confirmReceiveTicketOrderDistributor(Long orderDistributorId, OrderDistributorRequest orderDistributorRequest) throws ApiException, JsonProcessingException {
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        if (orderDistributorRequest.getProducts().isEmpty()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Vui lòng nhập thông tin sản phẩm");
        }
        OrderDistributor orderDistributor = oOrderDistributor.get();
        // Tạo phiếu nhập kho
        InboundTicket inboundTicket = new InboundTicket();
        String code = generatorCodeTicketService.generateCodeInOutBound(orderDistributor.getGarageId(), "MH", "NK", InboundType.ORDER_DISTRIBUTOR.getCode());
        String inboundCode = code + "-01";
        inboundTicket.setCode(inboundCode);
        inboundTicket.setType(InboundType.ORDER_DISTRIBUTOR.getCode());
        inboundTicket.setTicketId(orderDistributor.getId());
        inboundTicket.setDistributorId(orderDistributorRequest.getDistributorId());
        inboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
        inboundTicket.setGarageId(orderDistributor.getGarageId());
        inboundTicket = this.inboundTicketRepository.save(inboundTicket);

        List<OrderDistributorProduct> orderDistributorProducts = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal(0);

        for (OrderDistributorProductRequest orderDistributorProductRequest : orderDistributorRequest.getProducts()) {
            if (orderDistributorProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, PRODUCT_NOT_EXIST);
            }
            OrderDistributorProduct product = new OrderDistributorProduct();
            product.setOrderDistributorId(orderDistributor.getId());
            product.setProductId(orderDistributorProductRequest.getProductId());
            product.setUnit(orderDistributorProductRequest.getUnit());
            product.setQuantity(orderDistributorProductRequest.getQuantity() == null ? BigDecimal.ZERO : orderDistributorProductRequest.getQuantity());
            product.setUnitPrice(orderDistributorProductRequest.getUnitPrice());
            product.setDiscount(orderDistributorProductRequest.getDiscount());
            product.setTax(orderDistributorProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            BigDecimal price = orderDistributor.getDiscountType().equals(1) ? originPrice.subtract(orderDistributorProductRequest.getDiscount()).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(orderDistributorProductRequest.getDiscount())).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            orderDistributorProducts.add(product);
        }

        this.eventPublisher.publishReceiveProductInOrderDistributorEvent("receive_product_in_order_distributor", orderDistributorRequest);

        // Thêm phụ tùng vào phiếu nhập kho "inboundTicket"
        for (OrderDistributorProduct orderDistributorProduct : orderDistributorProducts) {
            InboundProduct inboundProduct = new InboundProduct();
            inboundProduct.setInboundTicketId(inboundTicket.getId());
            inboundProduct.setProductId(orderDistributorProduct.getProductId());
            inboundProduct.setUnit(orderDistributorProduct.getUnit());
            inboundProduct.setRequestQuantity(orderDistributorProduct.getQuantity());
            inboundProduct.setExportQuantity(BigDecimal.ZERO);
            inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
            inboundProduct = this.inboundProductRepository.save(inboundProduct);
            orderDistributorProduct.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
            orderDistributorProduct.setInboundProductId(inboundProduct.getId());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(orderDistributorProducts);

        this.saveLog(orderDistributorId, jsonProducts);

        orderDistributor.setProducts(jsonProducts);
        orderDistributor.setTotalPrice(totalPrice);
        orderDistributor.setDeliveryStatus(DeliverySparePartStatus.RECEIVE_PRODUCT.getCode());
        orderDistributorRepository.save(orderDistributor);
        return orderDistributorId;
    }

    public Long cancelTicketOrderDistributor(Long orderDistributorId) throws ApiException {
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        OrderDistributor orderDistributor = oOrderDistributor.get();
        orderDistributor.setDeliveryStatus(DeliverySparePartStatus.CANCELLED.getCode());
        orderDistributorRepository.save(orderDistributor);
        return orderDistributorId;
    }

    public Long refundTicketOrderDistributor(Long orderDistributorId) throws ApiException, JsonProcessingException {
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        OrderDistributor orderDistributor = oOrderDistributor.get();
        orderDistributor.setDeliveryStatus(DeliverySparePartStatus.REFUNDED.getCode());

        // Tạo phiếu xuất kho cho các sản phẩm
        ObjectMapper mapper = new ObjectMapper();
        String jsonProducts = orderDistributor.getProducts();
        OrderDistributorProduct[] listProducts = mapper.readValue(jsonProducts, OrderDistributorProduct[].class);

        List<OutboundProduct> outboundProducts = new ArrayList<>();
        for (OrderDistributorProduct orderDistributorProduct : listProducts) {
            // Nếu phụ tùng chưa nhập kho -> Huỷ phiếu đó
            if (orderDistributorProduct.getStatus().equals(InboundProductStatus.CHUA_NHAP.getCode())) {
                Optional<InboundProduct> oInboundProduct = this.inboundProductRepository.findById(orderDistributorProduct.getInboundProductId());
                InboundProduct inboundProduct = oInboundProduct.orElse(new InboundProduct());
                Optional<InboundTicket> oInboundTicket = this.inboundTicketRepository.findById(inboundProduct.getInboundTicketId());
                InboundTicket inboundTicket = oInboundTicket.orElse(new InboundTicket());
                inboundTicket.setStatus(InOutboundStatus.DELETED.getCode());
                this.inboundTicketRepository.save(inboundTicket);
            }

            // Nếu phụ tùng đã nhập kho -> Tạo phiếu xuất kho
            // Tạo thành 1 list sản phẩm để xuất kho
            if (orderDistributorProduct.getStatus().equals(InboundProductStatus.DA_NHAP.getCode())) {
                OutboundProduct outboundProduct = new OutboundProduct();
                outboundProduct.setProductId(orderDistributorProduct.getProductId());
                outboundProduct.setUnit(orderDistributorProduct.getUnit());
                outboundProduct.setRequestQuantity(orderDistributorProduct.getQuantity());
                outboundProduct.setExportQuantity(BigDecimal.ZERO);
                outboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                outboundProducts.add(outboundProduct);
            }
        }

        if (!outboundProducts.isEmpty()) {
            OutboundTicket outboundTicket = new OutboundTicket();
            String code = orderDistributor.getOrderCode();
            String[] splitedCode = code.split("MH");
            String outboundCode = splitedCode[0] + "XK" + OutboundType.FOR_REFUND.getCode() + splitedCode[1];
            int numberOfOutbound = this.outboundTicketRepository.countOutboundTicketByTicketIdAndType(orderDistributorId, OutboundType.FOR_REFUND.getCode()) + 1;
            outboundCode = outboundCode + "-" + String.format("%02d", numberOfOutbound);
            outboundTicket.setCode(outboundCode);
            outboundTicket.setType(OutboundType.FOR_REFUND.getCode());
            outboundTicket.setTicketId(orderDistributorId);
            outboundTicket.setGarageId(orderDistributor.getGarageId());
            outboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
            outboundTicket = this.outboundTicketRepository.save(outboundTicket);
            for (OutboundProduct outboundProduct : outboundProducts) {
                outboundProduct.setOutboundTicketId(outboundTicket.getId());
            }
            this.outboundProductRepository.saveAll(outboundProducts);
        }
        this.orderDistributorRepository.save(orderDistributor);
        return orderDistributorId;
    }


    public void validRequest(OrderDistributorRequest orderDistributorRequest) throws ApiException {
        if (Objects.isNull(orderDistributorRequest.getDistributorId()) && Objects.isNull(orderDistributorRequest.getDistributorCode())) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Thiếu thông tin NPP");
        }
        if (orderDistributorRequest.getProducts().isEmpty()) {
            throw new ApiException(ERROR.INVALID_REQUEST, "Đơn hàng đặt phải có sản phẩm");
        }
    }

    public void convertRequestToEntity(OrderDistributorRequest orderDistributorRequest, OrderDistributor orderDistributor) {
        orderDistributor.setDeliveryType(orderDistributorRequest.getDeliveryType());
        orderDistributor.setDiscount(orderDistributorRequest.getDiscount());
        orderDistributor.setTax(orderDistributorRequest.getTax());
        orderDistributor.setInventoryId(orderDistributorRequest.getInventoryId());
    }

    public void validPaymentStatus(int paymentStatus) throws ApiException {
        PaymentCompletedStatus paymentCompletedStatus = PaymentCompletedStatus.getPaymentCompletedStatus(paymentStatus);
        if (paymentCompletedStatus == null) {
            throw new ApiException(ERROR.BAD_REQUEST, "Thông tin trạng thái thanh toán không đúng");
        }
    }

    private void saveLog(Long orderDistributorId, String newJsonProducts) {
        LogOrderDistributor logOrderDistributor;
        Optional<LogOrderDistributor> oLogOrderDistributor = this.logOrderDistributorRepository.findFirstByOrderDistributorIdOrderByLogVersionDesc(orderDistributorId);
        if (oLogOrderDistributor.isPresent()) {
            logOrderDistributor = oLogOrderDistributor.get();
            LogOrderDistributor newLogOrderDistributor = new LogOrderDistributor();
            newLogOrderDistributor.setOrderDistributorId(orderDistributorId);
            newLogOrderDistributor.setLogVersion(logOrderDistributor.getLogVersion() + 1);
            newLogOrderDistributor.setProducts(newJsonProducts);
            newLogOrderDistributor.setActionUser(this.getKeyCloakUserId());
            this.logOrderDistributorRepository.save(newLogOrderDistributor);
        }
    }

    private void updateTicketOrderDistributor(OrderDistributorRequest orderDistributorRequest, Long orderDistributorId, Integer status) throws ApiException, JsonProcessingException {
        this.validRequest(orderDistributorRequest);
        Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(orderDistributorId);
        if (oOrderDistributor.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND, "Không tồn tại thông tin đơn hàng này");
        }
        OrderDistributor orderDistributor = oOrderDistributor.get();
        orderDistributor.setDistributorId(orderDistributorRequest.getDistributorId());
        this.convertRequestToEntity(orderDistributorRequest, orderDistributor);
        orderDistributor.setGarageId(orderDistributor.getGarageId());
        List<OrderDistributorProduct> orderDistributorProducts = new ArrayList<>();
        BigDecimal discount = new BigDecimal(0);
        BigDecimal tax = new BigDecimal(0);
        BigDecimal totalPrice = new BigDecimal(0);

        for (OrderDistributorProductRequest orderDistributorProductRequest : orderDistributorRequest.getProducts()) {
            if (orderDistributorProductRequest.getProductId() == 0) {
                throw new ApiException(ERROR.RESOURCE_NOT_FOUND, PRODUCT_NOT_EXIST);
            }
            OrderDistributorProduct product = new OrderDistributorProduct();
            product.setOrderDistributorId(orderDistributor.getId());
            product.setProductId(orderDistributorProductRequest.getProductId());
            product.setUnit(orderDistributorProductRequest.getUnit());
            product.setQuantity(orderDistributorProductRequest.getQuantity() == null ? BigDecimal.ZERO : orderDistributorProductRequest.getQuantity());
            product.setUnitPrice(orderDistributorProductRequest.getUnitPrice());
            product.setDiscount(orderDistributorProductRequest.getDiscount());
            product.setTax(orderDistributorProductRequest.getTax());
            BigDecimal originPrice = product.getQuantity().multiply(product.getUnitPrice());
            discount = orderDistributor.getDiscountType().equals(1) ? discount.add(orderDistributorProductRequest.getDiscount())
                    : discount.add(originPrice.multiply(orderDistributorProductRequest.getDiscount()));
            tax = tax.add(originPrice.multiply(product.getTax()));
            BigDecimal price = orderDistributor.getDiscountType().equals(1) ? originPrice.subtract(orderDistributorProductRequest.getDiscount()).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()))
                    : originPrice.multiply(new BigDecimal(1).subtract(orderDistributorProductRequest.getDiscount())).multiply(new BigDecimal(1).add(orderDistributorProductRequest.getTax()));
            product.setOriginalPrice(originPrice);
            product.setPrice(price);
            totalPrice = totalPrice.add(price);
            orderDistributorProducts.add(product);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonProducts = objectMapper.writeValueAsString(orderDistributorProducts);

        LogOrderDistributor logOrderDistributor = new LogOrderDistributor();
        logOrderDistributor.setOrderDistributorId(orderDistributor.getId());
        logOrderDistributor.setLogVersion(1);
        logOrderDistributor.setProducts(jsonProducts);
        logOrderDistributor.setActionUser(this.getKeyCloakUserId());
        this.logOrderDistributorRepository.save(logOrderDistributor);

        orderDistributor.setProducts(jsonProducts);
        orderDistributor.setDiscount(discount);
        orderDistributor.setTax(tax);
        orderDistributor.setTotalPrice(totalPrice);
        if (Objects.nonNull(status)) {
            orderDistributor.setDeliveryStatus(status);
        }
        this.orderDistributorRepository.save(orderDistributor);
    }
}
