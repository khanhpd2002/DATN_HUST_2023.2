package vn.com.cardoctor.garage_service.services.users;

import authentication.UserAuthentication;
import authentication.UserInfoContext;
import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.entities.garages.Garage;
import vn.com.cardoctor.garage_service.entities.garages.GarageGarageOwner;
import vn.com.cardoctor.garage_service.entities.garages.GarageOwner;
import vn.com.cardoctor.garage_service.models.requests.users.ChangePasswordRequest;
import vn.com.cardoctor.garage_service.models.requests.users.UserLoginRequest;
import vn.com.cardoctor.garage_service.models.responses.UserInfo;
import vn.com.cardoctor.garage_service.models.responses.users.AccessTokenResponse;
import vn.com.cardoctor.garage_service.repositories.garages.GarageGarageOwnerRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageOwnerRepository;
import vn.com.cardoctor.garage_service.repositories.garages.GarageRepository;
import vn.com.cardoctor.garage_service.rest_api.KeycloakRestApi;
import vn.com.cardoctor.garage_service.services.KeycloakService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static vn.com.cardoctor.garage_service.aop.LogAspect.USER_INFO;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final KeycloakRestApi keycloakRestApi;

    private final GarageOwnerRepository garageOwnerRepository;

    private final GarageGarageOwnerRepository garageGarageOwnerRepository;

    private final GarageRepository garageRepository;

    private final KeycloakService keycloakService;

    @Value("${keycloak.resource}")
    String clientId;

    @Value("${keycloak.credentials.secret}")
    String clientSecret;

    @Value("${my-keycloak.grant-type}")
    String grantType;

    public AccessTokenResponse login(UserLoginRequest loginRequest) throws ApiException {
        Map<String, String> data = new HashMap<>();
        data.put("client_id", clientId);
        data.put("grant_type", grantType);
        data.put("client_secret", clientSecret);
        data.put("username", loginRequest.getUsername());
        data.put("password", loginRequest.getPassword());
        try {
            return keycloakRestApi.login(data);
        } catch (Exception e) {
            throw new ApiException(401, "Invalid credentials");
        }
    }

    public UserInfo getUserInfo(HttpServletRequest httpServletRequest) throws ApiException {
        UserInfoContext.add(httpServletRequest.getHeader(USER_INFO));
        UserAuthentication userAuthentication = UserInfoContext.getUserInfo();
        log.info("userAuthentication is {}", userAuthentication);
        Optional<GarageOwner> oGarageOwner = this.garageOwnerRepository.findByKeycloakId(userAuthentication.getUserId());
        if (oGarageOwner.isEmpty()) {
            throw new ApiException(ERROR.INVALID_REQUEST);
        }
        GarageOwner garageOwner = oGarageOwner.get();
        UserInfo userInfo = new UserInfo();
        userInfo.setGarageOwner(garageOwner);
        List<GarageGarageOwner> garageGarageOwners = this.garageGarageOwnerRepository.findAllByGarageOwnerId(garageOwner.getId());
        List<Garage> garages = new ArrayList<>();
        for (GarageGarageOwner garageGarageOwner : garageGarageOwners) {
            Optional<Garage> oGarage = this.garageRepository.findById(garageGarageOwner.getGarageId());
            Garage garage = new Garage();
            if (oGarage.isPresent()) {
                garage = oGarage.get();
            }
            garages.add(garage);
        }
        userInfo.setGarages(garages);
        return userInfo;
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest) throws ApiException {
        this.keycloakService.changePassword(changePasswordRequest);
        return "Success";
    }

    public static JsonObject base64Decode(String encode) {
        String json = new String(Base64.getDecoder().decode(encode));
        return new JsonObject(json);
    }
}
