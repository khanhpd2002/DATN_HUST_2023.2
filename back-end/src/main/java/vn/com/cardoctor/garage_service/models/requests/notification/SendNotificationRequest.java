package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SendNotificationRequest {
    private Boolean allSubscription;
    private Integer type;
    private String deviceOs;
    private String deviceModel;
    private Map<String, String> data;
    private ApnsConfig apnsConfig;
    private NotificationRequest notification;
    private ScheduleNotificationRequest scheduleNotification;
    private List<Long> externalUserId;
}
