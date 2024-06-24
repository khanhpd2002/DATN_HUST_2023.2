package vn.com.cardoctor.garage_service.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private List<Objects> roles;
    private Boolean enabled;
    private Boolean mustChangePassword;
    private Integer attemptLoginFailed;
    private String systemRef;
    private String systemRefUserType;
    private Long systemRefUserId;
    private Boolean isAcceptSystemPolicy;
    private String oldToken;
    private String deactiveUser;
    private LocalDateTime deactiveDate;
    private String appName;
    private String appVersion;
    private String deviceType;
}

