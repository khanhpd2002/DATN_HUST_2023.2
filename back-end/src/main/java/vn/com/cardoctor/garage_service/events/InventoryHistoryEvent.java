package vn.com.cardoctor.garage_service.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class InventoryHistoryEvent extends ApplicationEvent {
    private String eventName;
    private Long inventoryHistoryId;
    private Long inventoryId;

    public InventoryHistoryEvent(Object source, String eventName, Long inventoryHistoryId, Long inventoryId) {
        super(source);
        this.eventName = eventName;
        this.inventoryHistoryId = inventoryHistoryId;
        this.inventoryId = inventoryId;
    }
}
