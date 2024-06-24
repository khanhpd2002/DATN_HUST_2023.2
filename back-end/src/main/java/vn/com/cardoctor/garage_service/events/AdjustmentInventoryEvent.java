package vn.com.cardoctor.garage_service.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import vn.com.cardoctor.garage_service.models.requests.inventories.InventoryHistoryRequest;

@Getter
@Setter
public class AdjustmentInventoryEvent extends ApplicationEvent {
    private String eventName;
    private InventoryHistoryRequest request;

    public AdjustmentInventoryEvent(Object source, String eventName, InventoryHistoryRequest request) {
        super(source);
        this.eventName = eventName;
        this.request = request;
    }
}
