package vn.com.cardoctor.garage_service.rest_api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import vn.com.cardoctor.garage_service.models.responses.users.AccessTokenResponse;

import java.util.Map;

@FeignClient(value = "keycloak", url = "${kc.admin.realms}")
public interface KeycloakRestApi {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AccessTokenResponse login(Map<String, ?> data);

}
