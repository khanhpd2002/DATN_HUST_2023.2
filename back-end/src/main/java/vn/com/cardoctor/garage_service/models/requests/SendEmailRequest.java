package vn.com.cardoctor.garage_service.models.requests;

import lombok.Data;

import java.util.List;

@Data
public class SendEmailRequest {
    private List<String> recipients;
    private String msgBody;
    private String subject;
    private String attachment;
}
