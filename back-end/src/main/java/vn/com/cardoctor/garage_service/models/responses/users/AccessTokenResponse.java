package vn.com.cardoctor.garage_service.models.responses.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("error_uri")
    private String errorUri;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("id_token")
    private String idToken;
    @JsonProperty("not-before-policy")
    private int notBeforePolicy;
    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("session_state")
    private String sessionState;
    @JsonProperty("token_type")
    private String tokenType;
}

