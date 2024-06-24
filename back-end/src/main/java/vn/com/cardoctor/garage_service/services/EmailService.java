package vn.com.cardoctor.garage_service.services;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.com.cardoctor.garage_service.entities.ProjectConfig;
import vn.com.cardoctor.garage_service.models.dtos.configs.SendEmailErrorConfigDto;
import vn.com.cardoctor.garage_service.models.requests.SendEmailRequest;
import vn.com.cardoctor.garage_service.repositories.ProjectConfigRepository;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class EmailService {
    @Value("${notification.service.url}")
    String notificationServiceUrl;

    @Value("${notification.email.username}")
    String notificationEmailUsername;

    @Value("${notification.email.password}")
    String notificationEmailPassword;

    @Autowired
    ProjectConfigRepository projectConfigRepository;

    @Autowired
    RestTemplate restTemplate;

    private final String SEND_EMAIL_CODE = "ERROR_MAIL";

    public HttpEntity<Object> buildAuthHeader(Object body, String notificationUsername, String notificationPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String authStr = notificationUsername + ":" + notificationPassword;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers.set("Authorization", "Basic " + base64Creds);
        return new HttpEntity<>(body, headers);
    }

    public BaseResponse sendEmailToUsers(SendEmailRequest sendEmailRequest) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(notificationServiceUrl);
        log.info("Email Request is {}", sendEmailRequest);
        // Gửi request và nhận response
        ResponseEntity<BaseResponse> response = restTemplate.exchange(notificationServiceUrl + "/emails/send-mail", HttpMethod.POST, this.buildAuthHeader(sendEmailRequest, notificationEmailUsername, notificationEmailPassword),
                BaseResponse.class);
        return response.getBody();
    }

    public void sendLogError(String stackTrace) {
        try {
            Optional<ProjectConfig> oProjectConfig = this.projectConfigRepository.findByCode(SEND_EMAIL_CODE);
            if (oProjectConfig.isPresent()) {
                ProjectConfig projectConfig = oProjectConfig.get();
                SendEmailErrorConfigDto sendEmailErrorConfigDto;
                sendEmailErrorConfigDto = new Gson().fromJson(projectConfig.getValue(), SendEmailErrorConfigDto.class);
                SendEmailRequest sendEmailRequest = new SendEmailRequest();
                sendEmailRequest.setSubject(sendEmailErrorConfigDto.getSubject());
                sendEmailRequest.setMsgBody(stackTrace);
                sendEmailRequest.setRecipients(sendEmailErrorConfigDto.getSendToUsers());
                this.sendEmailToUsers(sendEmailRequest);
            }
        }
        catch (Exception ex) {
            log.info("Send email fail, cause {}", ex.getMessage());
        }
    }
}

