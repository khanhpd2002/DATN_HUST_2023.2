package vn.com.cardoctor.garage_service.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AccessTokenResponse {
    @JsonAlias("access_token")
    private String accessToken;
}
