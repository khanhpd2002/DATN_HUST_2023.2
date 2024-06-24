package vn.com.cardoctor.garage_service.utils;

import javax.servlet.http.HttpServletRequest;
import vn.com.cardoctor.garage_service.models.requests.notification.ApnsConfig;
import vn.com.cardoctor.garage_service.models.requests.notification.NotificationRequest;
import vn.com.cardoctor.garage_service.models.requests.notification.ScheduleNotificationRequest;
import vn.com.cardoctor.garage_service.models.requests.notification.SendNotificationRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BuildNotificationUtil {
    public static SendNotificationRequest buildNotificationRequest(Boolean allSubscription, Integer type, String deviceOs,
                                                                   String deviceModel, Map<String, String> data, Integer badge,
                                                                   String alert, String category, Boolean contentAvailable,
                                                                   String title, String body, String image, Boolean immediately,
                                                                   String specificTime, String cronExpression, List<Long> externalUserId) {
        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setAllSubscription(allSubscription);
        sendNotificationRequest.setType(type);
        sendNotificationRequest.setData(data);
        sendNotificationRequest.setDeviceOs(deviceOs);
        sendNotificationRequest.setDeviceModel(deviceModel);

        ApnsConfig apnsConfig = new ApnsConfig();
        apnsConfig.setBadge(badge);
        apnsConfig.setAlert(alert);
        apnsConfig.setCategory(category);
        apnsConfig.setContentAvailable(contentAvailable);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setImage(image);

        ScheduleNotificationRequest scheduleNotificationRequest = new ScheduleNotificationRequest();
        scheduleNotificationRequest.setImmediately(immediately);
        scheduleNotificationRequest.setSpecificTime(specificTime);
        scheduleNotificationRequest.setCronExpression(cronExpression);

        sendNotificationRequest.setApnsConfig(apnsConfig);
        sendNotificationRequest.setNotification(notificationRequest);
        sendNotificationRequest.setScheduleNotification(scheduleNotificationRequest);
        sendNotificationRequest.setExternalUserId(externalUserId);

        return sendNotificationRequest;
    }

    public static SendNotificationRequest buildSimpleNotificationRequest(HttpServletRequest httpServerRequest, Map<String, String> data, String title, String body, String image, List<Long> externalUserId) {
        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setAllSubscription(true);
        sendNotificationRequest.setType(1);
        sendNotificationRequest.setData(data);
        String deviceOs = httpServerRequest.getHeader("device-type");
        if (Objects.isNull(deviceOs)) {
            deviceOs = "";
        }
        String appVersion = httpServerRequest.getHeader("app-version");
        if (Objects.isNull(appVersion)) {
            appVersion = "";
        }
        String appName = httpServerRequest.getHeader("app-name");
        if (Objects.isNull(appName)) {
            appName = "";
        }
        String deviceModel = appVersion + " " + appName;
        sendNotificationRequest.setDeviceOs(deviceOs);
        sendNotificationRequest.setDeviceModel(deviceModel);

        ApnsConfig apnsConfig = new ApnsConfig();
        apnsConfig.setBadge(1);
        apnsConfig.setAlert("");
        apnsConfig.setCategory("");
        apnsConfig.setContentAvailable(false);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setImage(image);

        ScheduleNotificationRequest scheduleNotificationRequest = new ScheduleNotificationRequest();
        scheduleNotificationRequest.setImmediately(true);
        scheduleNotificationRequest.setSpecificTime("");
        scheduleNotificationRequest.setCronExpression("");

        sendNotificationRequest.setApnsConfig(apnsConfig);
        sendNotificationRequest.setNotification(notificationRequest);
        sendNotificationRequest.setScheduleNotification(scheduleNotificationRequest);
        sendNotificationRequest.setExternalUserId(externalUserId);

        return sendNotificationRequest;
    }

    public static SendNotificationRequest scheduleNotification(HttpServletRequest httpServerRequest, Map<String, String> data, String title, String body, String image,
                                                               String specificTime, String cronExpression, List<Long> externalUserId) {
        SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
        sendNotificationRequest.setAllSubscription(true);
        sendNotificationRequest.setType(1);
        sendNotificationRequest.setData(data);
        String deviceOs = httpServerRequest.getHeader("device-type");
        if (Objects.isNull(deviceOs)) {
            deviceOs = "";
        }
        String appVersion = httpServerRequest.getHeader("app-version");
        if (Objects.isNull(appVersion)) {
            appVersion = "";
        }
        String appName = httpServerRequest.getHeader("app-name");
        if (Objects.isNull(appName)) {
            appName = "";
        }
        String deviceModel = appVersion + " " + appName;
        sendNotificationRequest.setDeviceOs(deviceOs);
        sendNotificationRequest.setDeviceModel(deviceModel);

        ApnsConfig apnsConfig = new ApnsConfig();
        apnsConfig.setBadge(1);
        apnsConfig.setAlert("");
        apnsConfig.setCategory("");
        apnsConfig.setContentAvailable(false);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setImage(image);

        ScheduleNotificationRequest scheduleNotificationRequest = new ScheduleNotificationRequest();
        scheduleNotificationRequest.setImmediately(false);
        scheduleNotificationRequest.setSpecificTime(specificTime);
        scheduleNotificationRequest.setCronExpression(cronExpression);

        sendNotificationRequest.setApnsConfig(apnsConfig);
        sendNotificationRequest.setNotification(notificationRequest);
        sendNotificationRequest.setScheduleNotification(scheduleNotificationRequest);
        sendNotificationRequest.setExternalUserId(externalUserId);

        return sendNotificationRequest;
    }

}
