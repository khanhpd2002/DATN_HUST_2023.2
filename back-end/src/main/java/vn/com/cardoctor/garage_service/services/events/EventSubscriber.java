package vn.com.cardoctor.garage_service.services.events;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.com.cardoctor.garage_service.entities.AdjustmentInventoryQuantityHistory;
import vn.com.cardoctor.garage_service.entities.ConfigPrice;
import vn.com.cardoctor.garage_service.entities.InventoryHistory;
import vn.com.cardoctor.garage_service.entities.Product;
import vn.com.cardoctor.garage_service.events.AdjustmentInventoryEvent;
import vn.com.cardoctor.garage_service.events.InventoryHistoryEvent;
import vn.com.cardoctor.garage_service.events.OrderDistributorEvent;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorProductRequest;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorRequest;
import vn.com.cardoctor.garage_service.repositories.AdjustmentInventoryQuantityHistoryRepository;
import vn.com.cardoctor.garage_service.repositories.ConfigPriceRepository;
import vn.com.cardoctor.garage_service.repositories.InventoryHistoryRepository;
import vn.com.cardoctor.garage_service.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventSubscriber {
    private final ProductRepository productRepository;

    private final ConfigPriceRepository configPriceRepository;

    private final InventoryHistoryRepository inventoryHistoryRepository;

    private final AdjustmentInventoryQuantityHistoryRepository adjustmentInventoryQuantityHistoryRepository;

    @Async
    @EventListener(condition = "event.eventName.equals('create_order_distributor')")
    public void onListenCreateOrderDistributor(OrderDistributorEvent event) {
        OrderDistributorRequest orderDistributorRequest = event.getRequest();
        List<OrderDistributorProductRequest> products = orderDistributorRequest.getProducts();
        for (OrderDistributorProductRequest productRequest : products) {
            Optional<Product> oParentProduct = this.productRepository.findById(productRequest.getProductId());
            Product parentProduct = oParentProduct.orElse(new Product());
            parentProduct.setPurchasePrice(productRequest.getUnitPrice());
            this.productRepository.save(parentProduct);
            Optional<Product> oProduct = this.productRepository.findByParentProductIdAndDistributorId(productRequest.getProductId(),
                    orderDistributorRequest.getDistributorId());
            Product product = oProduct.orElse(new Product());
            product.setPurchasePrice(productRequest.getUnitPrice());
            this.productRepository.save(product);
            log.info("====== Update purchase price product success !!!! ======");
        }
    }

    @Async
    @EventListener(condition = "event.eventName.equals('adjustment_inventory_event')")
    public void onListenAdjustmentInventory(AdjustmentInventoryEvent event) {
        InventoryHistoryRequest request = event.getRequest();
        List<Product> products = new ArrayList<>();
        request.getInventoryHistoryDetails().forEach(inventoryHistoryDetailRequest -> {
            Optional<Product> oParentProduct = this.productRepository.findById(inventoryHistoryDetailRequest.getProductId());
            Product parentProduct = oParentProduct.orElse(new Product());
            parentProduct.setQuantity(inventoryHistoryDetailRequest.getRealityInventory());
            products.add(parentProduct);
        });
        this.productRepository.saveAll(products);
        log.info("===== update quantity in adjustment inventory success =====");
        AdjustmentInventoryQuantityHistory adjustmentInventoryQuantityHistory = new AdjustmentInventoryQuantityHistory();
        String value = new Gson().toJson(request.getInventoryHistoryDetails());
        adjustmentInventoryQuantityHistory.setValue(value);
        this.adjustmentInventoryQuantityHistoryRepository.save(adjustmentInventoryQuantityHistory);
    }

    @Async
    @EventListener(condition = "event.eventName.equals('create_new_inventory_history')")
    public void onListenCreateInventoryHistory(InventoryHistoryEvent event) {
        Long inventoryHistoryId = event.getInventoryHistoryId();
        Long inventoryId = event.getInventoryId();
        List<InventoryHistory> inventoryHistoryList = new ArrayList<>();
        Iterable<InventoryHistory> inventoryHistories = this.inventoryHistoryRepository.findAllByInventoryId(inventoryId);
        inventoryHistories.forEach(inventoryHistory -> {
            inventoryHistory.setCanAdjustment(false);
            inventoryHistoryList.add(inventoryHistory);
        });
        this.inventoryHistoryRepository.saveAll(inventoryHistoryList);
        Optional<InventoryHistory> oInventoryHistory = this.inventoryHistoryRepository.findById(inventoryHistoryId);
        InventoryHistory inventoryHistory = oInventoryHistory.orElse(new InventoryHistory());
        inventoryHistory.setCanAdjustment(true);
        this.inventoryHistoryRepository.save(inventoryHistory);
        log.info("===== run job when create inventory history success =====");
    }

    @Async
    @EventListener(condition = "event.eventName.equals('receive_product_in_order_distributor')")
    public void onListenReceiveProductInOrderDistributor(OrderDistributorEvent event) {
        OrderDistributorRequest orderDistributorRequest = event.getRequest();
        List<OrderDistributorProductRequest> products = orderDistributorRequest.getProducts();
        for (OrderDistributorProductRequest productRequest : products) {
            List<ConfigPrice> configPrices = this.configPriceRepository.findAllByProductId(productRequest.getProductId());
            for (ConfigPrice configPrice : configPrices) {
                configPrice.setPrice(productRequest.getUnitPrice());
            }
            this.configPriceRepository.saveAll(configPrices);
        }
        log.info("===== run job when update config price success =====");
    }
}
