package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Integer type;
    private String token;
    private String deviceModel;
    private String deviceOs;
    private String metaData;
    private UserRequest user;
}
