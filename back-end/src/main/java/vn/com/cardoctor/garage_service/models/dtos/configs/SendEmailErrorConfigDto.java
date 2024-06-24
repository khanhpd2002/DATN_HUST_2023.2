package vn.com.cardoctor.garage_service.models.dtos.configs;

import lombok.Data;

import java.util.List;

@Data
public class SendEmailErrorConfigDto {
    private String subject;
    private List<String> sendToUsers;
}
