package vn.com.cardoctor.garage_service.models.responses;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ActionLogResponse {
    private String uri;
    private String method;
    private Integer status;
    private String hostIp;
    private String remoteIp;
    private Long time;
    private String requestPayload;
    private String responsePayload;
    private String requestParams;
}
