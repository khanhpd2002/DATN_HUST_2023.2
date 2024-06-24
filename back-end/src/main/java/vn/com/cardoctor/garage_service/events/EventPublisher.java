package vn.com.cardoctor.garage_service.events;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorRequest;

@Component
@Log4j2
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final String eventName, final OrderDistributorRequest request) {
        log.info("Publishing custom event {}", eventName);
        OrderDistributorEvent event = new OrderDistributorEvent(this, eventName, request);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishAdjustmentInventoryEvent(final String eventName, final InventoryHistoryRequest request) {
        log.info("Publishing custom adjustment inventory event {}", eventName);
        AdjustmentInventoryEvent event = new AdjustmentInventoryEvent(this, eventName, request);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishCreateNewInventoryHistoryEvent(final String eventName, final Long inventoryHistoryId, Long inventoryId) {
        log.info("Publishing custom create new inventory history event {}", eventName);
        InventoryHistoryEvent event = new InventoryHistoryEvent(this, eventName, inventoryHistoryId, inventoryId);
        applicationEventPublisher.publishEvent(event);
    }

    public void publishReceiveProductInOrderDistributorEvent(final String eventName, final OrderDistributorRequest request) {
        log.info("Publishing receive product in order distributor {}", eventName);
        OrderDistributorEvent event = new OrderDistributorEvent(this, eventName, request);
        applicationEventPublisher.publishEvent(event);
    }
}
