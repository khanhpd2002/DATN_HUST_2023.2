package vn.com.cardoctor.garage_service.entities.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CmsAuthTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("scope")
    private String scope;

}

