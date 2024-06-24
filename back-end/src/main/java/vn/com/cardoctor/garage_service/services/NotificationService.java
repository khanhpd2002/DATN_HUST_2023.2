package vn.com.cardoctor.garage_service.services;

import lombok.extern.log4j.Log4j2;
import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.com.cardoctor.garage_service.entities.NotificationConfig;
import vn.com.cardoctor.garage_service.models.requests.notification.SendNotificationRequest;
import vn.com.cardoctor.garage_service.models.requests.notification.SubscriptionRequest;
import vn.com.cardoctor.garage_service.repositories.NotificationConfigRepository;

import java.util.Base64;
import java.util.Optional;

@Service
@Log4j2
public class NotificationService {
    @Value("${notification.service.url}")
    String notificationServiceUrl;

    @Value("${notification.garage.username}")
    String notificationGarageUsername;

    @Value("${notification.garage.password}")
    String notificationGaragePassword;

    @Value("${notification.driver.username}")
    String notificationDriverUsername;

    @Value("${notification.driver.password}")
    String notificationDriverPassword;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    NotificationConfigRepository notificationConfigRepository;


    public HttpEntity<Object> buildAuthHeader(Object body, String notificationUsername, String notificationPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String authStr = notificationUsername + ":" + notificationPassword;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers.set("Authorization", "Basic " + base64Creds);
        return new HttpEntity<>(body, headers);
    }

    public NotificationConfig findByNotification(String notification) {
        Optional<NotificationConfig> oNotificationConfig = this.notificationConfigRepository.findByNotification(notification);
        if (oNotificationConfig.isEmpty()) {
            return new NotificationConfig();
        }
        return oNotificationConfig.get();
    }

    public BaseResponse createSubscriptionGarage(SubscriptionRequest subscriptionRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(notificationServiceUrl);

        // Gửi request và nhận response
        ResponseEntity<BaseResponse> response = restTemplate.exchange(notificationServiceUrl + "/subscriptions", HttpMethod.POST, this.buildAuthHeader(subscriptionRequest, notificationGarageUsername, notificationGaragePassword),
                BaseResponse.class);
        return response.getBody();
    }

    public BaseResponse createSubscriptionDriver(SubscriptionRequest subscriptionRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(notificationServiceUrl);

        // Gửi request và nhận response
        ResponseEntity<BaseResponse> response = restTemplate.exchange(notificationServiceUrl + "/subscriptions", HttpMethod.POST, this.buildAuthHeader(subscriptionRequest, notificationDriverUsername, notificationDriverPassword),
                BaseResponse.class);
        return response.getBody();
    }

    public BaseResponse sendNotificationToGarage(SendNotificationRequest sendNotificationRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(notificationServiceUrl);
        log.info("Notification Request is {}", sendNotificationRequest);
        // Gửi request và nhận response
        ResponseEntity<BaseResponse> response = restTemplate.exchange(notificationServiceUrl + "/notifications", HttpMethod.POST, this.buildAuthHeader(sendNotificationRequest, notificationGarageUsername, notificationGaragePassword),
                BaseResponse.class);
        return response.getBody();
    }

    public BaseResponse sendNotificationToDriver(SendNotificationRequest sendNotificationRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(notificationServiceUrl);

        // Gửi request và nhận response
        ResponseEntity<BaseResponse> response = restTemplate.exchange(notificationServiceUrl + "/notifications", HttpMethod.POST, this.buildAuthHeader(sendNotificationRequest, notificationDriverUsername, notificationDriverPassword),
                BaseResponse.class);
        return response.getBody();
    }
}
