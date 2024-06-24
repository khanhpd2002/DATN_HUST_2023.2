package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

import java.util.Map;

@Data
public class BaseMessageFirebaseRequest {
    private Map<String, String> data;
    private ApnsConfig apnsConfig;
    private NotificationRequest notification;
    private ScheduleNotificationRequest scheduleNotification;
}
