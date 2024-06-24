package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

@Data
public class ScheduleNotificationRequest {
    private boolean isImmediately;
    private String specificTime;
    private String cronExpression;
}
