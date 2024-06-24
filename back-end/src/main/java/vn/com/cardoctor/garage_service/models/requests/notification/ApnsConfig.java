package vn.com.cardoctor.garage_service.models.requests.notification;

import lombok.Data;

@Data
public class ApnsConfig {
    private Integer badge;
    private String alert;
    private String category;
    private Boolean contentAvailable;
}
