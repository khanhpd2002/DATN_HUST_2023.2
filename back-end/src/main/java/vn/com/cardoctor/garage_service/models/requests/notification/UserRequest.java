package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

@Data
public class UserRequest {
    private Long externalUserId;
    private Long lat;
    private Long lon;
    private String timezoneId;
    private String country;
    private String email;
    private String phoneNumber;
    private String metaData;
}
