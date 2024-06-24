package vn.com.cardoctor.garage_service.models.requests.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("client_id")
    private String clientId;
    private String username;
    private String password;
    private String grantType;
    @JsonProperty("client_secret")
    private String clientSecret;
}
