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
import vn.com.cardoctor.garage_service.entities.*;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.orders_part.OrderDistributor;
import vn.com.cardoctor.garage_service.enums.*;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.InboundProductRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.InboundReferenceProductRequest;
import vn.com.cardoctor.garage_service.models.requests.in_out_bound.InboundTicketRequest;
import vn.com.cardoctor.garage_service.models.requests.product.ProductRequest;
import vn.com.cardoctor.garage_service.models.responses.in_out_bound.InboundTicketResponse;
import vn.com.cardoctor.garage_service.repositories.InventoryRepository;
import vn.com.cardoctor.garage_service.repositories.ProductLogRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundProductRepository;
import vn.com.cardoctor.garage_service.repositories.in_out_bounds.InboundTicketRepository;
import vn.com.cardoctor.garage_service.repositories.orders_part.OrderDistributorRepository;
import vn.com.cardoctor.garage_service.services.BaseService;
import vn.com.cardoctor.garage_service.services.GeneratorCodeTicketService;
import vn.com.cardoctor.garage_service.services.ProductService;
import vn.com.cardoctor.garage_service.utils.StringUtil;

import java.math.BigDecimal;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class InboundService extends BaseService {
    private final InboundTicketRepository inboundTicketRepository;
    private final InboundProductRepository inboundProductRepository;
    private final ProductRepository productRepository;
    private final ProductLogRepository productLogRepository;
    private final OrderDistributorRepository orderDistributorRepository;
    private final GeneratorCodeTicketService generatorCodeTicketService;
    private final InventoryRepository inventoryRepository;
    private final GarageRepository garageRepository;
    private final ProductService productService;

    public PagingDataResponse findAllInboundTicket(Long garageId, Date fromDate, Date toDate, Integer type, String code, Integer status,
                                                    Integer pageSize, Integer pageNumber) {
        return this.inboundTicketRepository.findAllInboundTicket(garageId, fromDate, toDate, type, code, status, pageSize, pageNumber);
    }

    public InboundTicketResponse getDetailInboundTicket(Long inboundTicketId) throws ApiException {
        Optional<InboundTicket> oInboundTicket = this.inboundTicketRepository.findById(inboundTicketId);
        if (oInboundTicket.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        InboundTicket inboundTicket = oInboundTicket.get();
        InboundTicketResponse response = new InboundTicketResponse();
        response.setId(inboundTicket.getId());
        response.setType(inboundTicket.getType());
        response.setCode(inboundTicket.getCode());
        response.setCreatedAt(inboundTicket.getCreatedAt());
        response.setTicketId(inboundTicket.getTicketId());
        response.setStatus(inboundTicket.getStatus());
        response.setDistributorId(inboundTicket.getDistributorId());

        List<InboundProduct> inboundProducts = this.inboundProductRepository.findByInboundTicketId(inboundTicketId);
        response.setInboundProducts(inboundProducts);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long create(Long garageId, InboundTicketRequest request) throws ApiException, JsonProcessingException {
        if (request.getType() == null) {
            throw new ApiException(ERROR.BAD_REQUEST);
        }
        InboundTicket inboundTicket = new InboundTicket();
        String code = this.generatorCodeTicketService.generateTicketCode(garageId, "NK");
        String[] splitedCode = code.split("NK");
        String inboundCode = splitedCode[0] + "NK" + request.getType() + splitedCode[1];
        inboundTicket.setCode(inboundCode);
        inboundTicket.setType(request.getType());
        inboundTicket.setDistributorId(request.getDistributorId());
        inboundTicket.setTicketId(request.getTicketId());
        inboundTicket.setNote(request.getNote());
        inboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
        inboundTicket.setGarageId(garageId);
        inboundTicket = this.inboundTicketRepository.save(inboundTicket);
        List<InboundProduct> inboundProducts = new ArrayList<>();
        for (InboundProductRequest inboundProductRequest : request.getInboundProducts()) {
            InboundProduct inboundProduct = new InboundProduct();
            if (inboundProductRequest.getProductId() == null) {
                Garage garage = this.garageRepository.findById(garageId).get();
                Inventory inventory = this.inventoryRepository.findFirstByGarageId(garageId).get();
                ProductRequest product = new ProductRequest();
                product.setName(inboundProductRequest.getProductName());
                product.setCode(garage.getCode() + "PT" + StringUtil.generateNumericString(6));
                product.setUnit(inboundProductRequest.getUnit());
                inboundProduct.setProductId(this.productService.create(product, inventory.getId()).getParentProductId());
            } else {
                inboundProduct.setProductId(inboundProductRequest.getProductId());
            }
            inboundProduct.setInboundTicketId(inboundTicket.getId());
            inboundProduct.setUnit(inboundProductRequest.getUnit());
            inboundProduct.setRequestQuantity(inboundProductRequest.getRequestQuantity());
            inboundProduct.setExportQuantity(inboundProductRequest.getExportQuantity());
            inboundProduct.setNote(inboundProductRequest.getNote());
            inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
            inboundProduct.setDistributorId(request.getDistributorId());
            inboundProducts.add(inboundProduct);
        }
        String products = new ObjectMapper().writeValueAsString(inboundProducts);
        inboundTicket.setProducts(products);
        this.inboundProductRepository.saveAll(inboundProducts);
        return inboundTicket.getId();
    }

    public Long confirmAction(Long garageId, Long inboundTicketId, InboundTicketRequest request) throws ApiException, JsonProcessingException {
        boolean isUpdate = false;
        boolean isCreateNewInbound = false;
        String actionUser = this.getKeyCloakUserId();
        Optional<InboundTicket> oInboundTicket = this.inboundTicketRepository.findById(inboundTicketId);
        if (oInboundTicket.isEmpty()) {
            throw new ApiException(ERROR.RESOURCE_NOT_FOUND);
        }
        InboundTicket inboundTicket = oInboundTicket.get();
        if (inboundTicket.getStatus().equals(InOutboundStatus.VERIFIED.getCode())) {
            throw new ApiException(ERROR.BAD_REQUEST);
        }

        OrderDistributor orderDistributor = new OrderDistributor();

        List<InboundReferenceProductRequest> inboundReferenceProductRequests = new ArrayList<>();
        List<InboundReferenceProductRequest> newInboundReferenceProductRequests = new ArrayList<>();

        if (inboundTicket.getType().equals(InboundType.ORDER_DISTRIBUTOR.getCode()) && inboundTicket.getTicketId() != null && !inboundTicket.getTicketId().equals(0L)) {
            Optional<OrderDistributor> oOrderDistributor = this.orderDistributorRepository.findById(inboundTicket.getTicketId());
            orderDistributor = oOrderDistributor.orElse(new OrderDistributor());
            String content = orderDistributor.getProducts();
            inboundReferenceProductRequests = new ObjectMapper().readValue(content, new TypeReference<>(){});
            newInboundReferenceProductRequests = new ArrayList<>(inboundReferenceProductRequests);
        }

        List<InboundProduct> inboundProducts = this.inboundProductRepository.findByInboundTicketId(inboundTicketId);
        List<InboundProduct> newInboundProducts = new ArrayList<>();

        // Loop qua từng vật tư trong phiếu nhập kho
        for (int i = 0; i < request.getInboundProducts().size(); i++) {
            if (request.getInboundProducts().get(i).getExportQuantity() == null) {
                throw new ApiException(ERROR.BAD_REQUEST, "Vui lòng nhập số lượng");
            }
            // Số lượng thực tế > số lượng yêu cầu -> Exception
            if (inboundProducts.get(i).getRequestQuantity().compareTo(request.getInboundProducts().get(i).getExportQuantity()) < 0) {
                throw new ApiException(ERROR.BAD_REQUEST, "Số lượng nhập thực tế không được nhiều hơn số lượng yêu cầu");
            }

            inboundProducts.get(i).setExportQuantity(request.getInboundProducts().get(i).getExportQuantity());
            inboundProducts.get(i).setNote(request.getInboundProducts().get(i).getNote());
            inboundProducts.get(i).setStatus(InboundProductStatus.DA_NHAP.getCode());

            // Nếu có thay đổi số lượng thực tế -> Đánh dấu là đã update -> Sẽ close phiếu sau khi update
            if (request.getInboundProducts().get(i).getExportQuantity().compareTo(BigDecimal.ZERO) > 0) {
                isUpdate = true;
            }

            // Nếu SL thực tế < SL yêu cầu -> Save lại thông tin mới với SL yêu cầu = SL yêu cầu cũ - SL đã xuất thực tế
            if (inboundProducts.get(i).getRequestQuantity().compareTo(request.getInboundProducts().get(i).getExportQuantity()) > 0) {
                InboundProduct inboundProduct = new InboundProduct();
                inboundProduct.setProductId(inboundProducts.get(i).getProductId());
                inboundProduct.setUnit(inboundProducts.get(i).getUnit());
                inboundProduct.setDistributorId(inboundProducts.get(i).getDistributorId());
                inboundProduct.setRequestQuantity(inboundProducts.get(i).getRequestQuantity().subtract(request.getInboundProducts().get(i).getExportQuantity()));
                inboundProduct.setExportQuantity(BigDecimal.ZERO);
                inboundProduct.setStatus(InboundProductStatus.CHUA_NHAP.getCode());
                inboundProduct = this.inboundProductRepository.save(inboundProduct);

                if (inboundTicket.getTicketId() != null) {
                    for (InboundReferenceProductRequest inboundReferenceProductRequest : inboundReferenceProductRequests) {
                        if (inboundReferenceProductRequest.getInboundProductId().equals(inboundProducts.get(i).getId())) {
                            // Thay đổi số lượng trong phiếu dịch vụ, set lại trạng thái và tổng tiền
                            if (!inboundProducts.get(i).getExportQuantity().equals(BigDecimal.ZERO)) {
                                InboundReferenceProductRequest productRequest = new InboundReferenceProductRequest();
                                productRequest.setOrderDistributorId(inboundReferenceProductRequest.getOrderDistributorId());
                                productRequest.setProductId(inboundReferenceProductRequest.getProductId());
                                productRequest.setUnit(inboundReferenceProductRequest.getUnit());
                                productRequest.setQuantity(request.getInboundProducts().get(i).getExportQuantity());
                                productRequest.setUnitPrice(inboundReferenceProductRequest.getUnitPrice());
                                productRequest.setDiscount(inboundReferenceProductRequest.getDiscount());
                                productRequest.setTax(inboundReferenceProductRequest.getTax());
                                productRequest.setOriginalPrice(productRequest.getUnitPrice().multiply(productRequest.getQuantity()));
                                productRequest.setPrice(calcTotalPrice(productRequest.getUnitPrice(), productRequest.getQuantity(),
                                        productRequest.getDiscount(), productRequest.getTax()));
                                productRequest.setStatus(OutboundProductStatus.DA_XUAT.getCode());
                                productRequest.setInboundProductId(inboundReferenceProductRequest.getInboundProductId());
                                newInboundReferenceProductRequests.add(productRequest);

                                newInboundReferenceProductRequests.remove(inboundReferenceProductRequest);
                                // Thêm mới vật tư trong phiếu dịch vụ với số lượng bằng phần chưa xuất kho, trạng thái chưa xuất
                                InboundReferenceProductRequest newInboundReferenceProductRequest = new InboundReferenceProductRequest();
                                newInboundReferenceProductRequest.setOrderDistributorId(inboundReferenceProductRequest.getOrderDistributorId());
                                newInboundReferenceProductRequest.setProductId(inboundReferenceProductRequest.getProductId());
                                newInboundReferenceProductRequest.setUnit(inboundReferenceProductRequest.getUnit());
                                newInboundReferenceProductRequest.setQuantity(inboundProducts.get(i).getRequestQuantity().subtract(request.getInboundProducts().get(i).getExportQuantity()));
                                newInboundReferenceProductRequest.setUnitPrice(inboundReferenceProductRequest.getUnitPrice());
                                newInboundReferenceProductRequest.setDiscount(BigDecimal.ZERO);
                                newInboundReferenceProductRequest.setTax(inboundReferenceProductRequest.getTax());
                                newInboundReferenceProductRequest.setOriginalPrice(newInboundReferenceProductRequest.getUnitPrice().multiply(newInboundReferenceProductRequest.getQuantity()));
                                newInboundReferenceProductRequest.setPrice(calcTotalPrice(newInboundReferenceProductRequest.getUnitPrice(), newInboundReferenceProductRequest.getQuantity(),
                                        newInboundReferenceProductRequest.getDiscount(), newInboundReferenceProductRequest.getTax()));
                                newInboundReferenceProductRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                                newInboundReferenceProductRequest.setInboundProductId(inboundProduct.getId());
                                newInboundReferenceProductRequests.add(newInboundReferenceProductRequest);
                            } else {
                                InboundReferenceProductRequest productRequest = new InboundReferenceProductRequest();
                                productRequest.setOrderDistributorId(inboundReferenceProductRequest.getOrderDistributorId());
                                productRequest.setProductId(inboundReferenceProductRequest.getProductId());
                                productRequest.setUnit(inboundReferenceProductRequest.getUnit());
                                productRequest.setQuantity(request.getInboundProducts().get(i).getRequestQuantity());
                                productRequest.setUnitPrice(inboundReferenceProductRequest.getUnitPrice());
                                productRequest.setDiscount(inboundReferenceProductRequest.getDiscount());
                                productRequest.setTax(inboundReferenceProductRequest.getTax());
                                productRequest.setOriginalPrice(inboundReferenceProductRequest.getUnitPrice().multiply(inboundReferenceProductRequest.getQuantity()));
                                productRequest.setPrice(calcTotalPrice(inboundReferenceProductRequest.getUnitPrice(), inboundReferenceProductRequest.getQuantity(),
                                        inboundReferenceProductRequest.getDiscount(), inboundReferenceProductRequest.getTax()));
                                productRequest.setStatus(OutboundProductStatus.CHUA_XUAT.getCode());
                                productRequest.setInboundProductId(inboundProduct.getId());
                                newInboundReferenceProductRequests.add(productRequest);

                                newInboundReferenceProductRequests.remove(inboundReferenceProductRequest);
                            }

                        }
                    }
                }

                // Tăng số lượng tồn kho phụ tùng
                Optional<Product> oProduct = this.productRepository.findById(inboundProducts.get(i).getProductId());
                Product product = oProduct.orElse(new Product());
                product.setQuantity(product.getQuantity().add(request.getInboundProducts().get(i).getExportQuantity()));

                // Lưu log
                int finalI = i;
                Thread thread = new Thread(() -> this.saveProductLog(actionUser, product, product.getQuantity().add(request.getInboundProducts().get(finalI).getExportQuantity())));
                thread.start();
                this.productRepository.save(product);

//                inboundProduct = this.inboundProductRepository.save(inboundProduct);
                newInboundProducts.add(inboundProduct);
                isCreateNewInbound = true;
            }

            // Nếu SL thực tế = SL yêu cầu -> Đổi trạng thái của sản phẩm này trong phiếu DV thành đã xuất kho
            if (inboundProducts.get(i).getRequestQuantity().compareTo(request.getInboundProducts().get(i).getExportQuantity()) == 0) {
                if (inboundTicket.getTicketId() != null && !inboundTicket.getTicketId().equals(0L)) {
                    for (InboundReferenceProductRequest inboundReferenceProductRequest : inboundReferenceProductRequests) {
                        if (inboundReferenceProductRequest.getInboundProductId().equals(inboundProducts.get(i).getId())) {
                            inboundReferenceProductRequest.setStatus(InboundProductStatus.DA_NHAP.getCode());
                        }
                    }
                }
                // Tăng số lượng tồn kho phụ tùng
                Optional<Product> oProduct = this.productRepository.findById(inboundProducts.get(i).getProductId());
                Product product = oProduct.orElse(new Product());
                product.setQuantity(product.getQuantity().add(request.getInboundProducts().get(i).getExportQuantity()));
                // Lưu log
                int finalI = i;
                Thread thread = new Thread(() -> this.saveProductLog(actionUser, product, product.getQuantity().add(request.getInboundProducts().get(finalI).getExportQuantity())));
                thread.start();
                this.productRepository.save(product);
            }

        }

        if (isUpdate) {
            inboundTicket.setStatus(InOutboundStatus.VERIFIED.getCode());
        }
        if (isCreateNewInbound && isUpdate) {
            InboundTicket newInboundTicket = new InboundTicket();
            if (inboundTicket.getTicketId().equals(0L)) {
                String code = this.generatorCodeTicketService.generateTicketCode(garageId, "NK");
                String[] splitedCode = code.split("NK");
                String inboundCode = splitedCode[0] + "NK" + request.getType() + splitedCode[1];
                newInboundTicket.setCode(inboundCode);
            } else {
                String[] oldCode = inboundTicket.getCode().split("-");
                String code = oldCode[1];
                String newCode = oldCode[0] + "-" + StringUtil.getNextValue(code);
                newInboundTicket.setCode(newCode);
            }
            newInboundTicket.setType(inboundTicket.getType());
            newInboundTicket.setDistributorId(inboundTicket.getDistributorId());
            newInboundTicket.setTicketId(inboundTicket.getTicketId());
            newInboundTicket.setNote(inboundTicket.getNote());
            newInboundTicket.setStatus(InOutboundStatus.UN_VERIFIED.getCode());
            newInboundTicket.setGarageId(garageId);
            newInboundTicket = this.inboundTicketRepository.save(newInboundTicket);
            for (InboundProduct inboundProduct : newInboundProducts) {
                inboundProduct.setInboundTicketId(newInboundTicket.getId());
            }
            this.inboundProductRepository.saveAll(newInboundProducts);
        }
        String listProductStr = new ObjectMapper().writeValueAsString(newInboundReferenceProductRequests);

        // Nếu phiếu nhập kho ứng với đơn mua hàng
        // Lưu lại thông tin vào products trong phiếu mua hàng
        if (inboundTicket.getType().equals(InboundType.ORDER_DISTRIBUTOR.getCode()) && inboundTicket.getTicketId() != null && !inboundTicket.getTicketId().equals(0L)) {
            orderDistributor.setProducts(listProductStr);
            this.orderDistributorRepository.save(orderDistributor);
        }

        this.inboundProductRepository.saveAll(inboundProducts);
        this.inboundTicketRepository.save(inboundTicket);
        return inboundTicketId;
    }

    public BigDecimal getNumberTicketRefundRate(Long garageId, String fromDate, String toDate) {
        return inboundTicketRepository.getNumberTicketRefundRate(garageId, fromDate, toDate);
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
