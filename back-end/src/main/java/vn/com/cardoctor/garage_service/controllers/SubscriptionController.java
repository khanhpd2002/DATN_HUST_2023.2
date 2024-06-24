package vn.com.cardoctor.garage_service.controllers;

import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.notification.SubscriptionRequest;
import vn.com.cardoctor.garage_service.services.NotificationService;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/garage")
    public BaseResponse createSubscriptionGarage(@RequestBody SubscriptionRequest request) throws ApiException {
        return notificationService.createSubscriptionGarage(request);
    }

    @PostMapping("/driver")
    public BaseResponse createSubscriptionDriver(@RequestBody SubscriptionRequest request) throws ApiException {
        return notificationService.createSubscriptionDriver(request);
    }
}
