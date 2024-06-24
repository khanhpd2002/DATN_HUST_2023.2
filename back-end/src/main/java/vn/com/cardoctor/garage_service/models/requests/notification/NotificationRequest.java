package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String body;
    private String image;
}
