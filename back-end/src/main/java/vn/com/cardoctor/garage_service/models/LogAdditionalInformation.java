package vn.com.cardoctor.garage_service.models;

import lombok.Data;

@Data
public class LogAdditionalInformation {
    private String uri;
    private String method;
    private String remoteIp;
    private String hostIp;
    private Long time;
}
