package vn.com.cardoctor.garage_service.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import vn.com.cardoctor.garage_service.models.requests.orders_part.OrderDistributorRequest;

@Getter
@Setter
public class OrderDistributorEvent extends ApplicationEvent {
    private String eventName;
    private OrderDistributorRequest request;

    public OrderDistributorEvent(Object source, String eventName, OrderDistributorRequest request) {
        super(source);
        this.eventName = eventName;
        this.request = request;
    }
}
