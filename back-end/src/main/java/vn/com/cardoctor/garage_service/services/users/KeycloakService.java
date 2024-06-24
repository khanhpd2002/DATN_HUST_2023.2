//package vn.com.cardoctor.garage_service.services.users;
//
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.CredentialRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import vn.com.cardoctor.garage_service.utils.KeycloakUtil;
//
//import javax.ws.rs.core.Response;
//import java.util.Collections;
//
//@Service
//public class KeycloakService {
//    @Value("${kc.admin.realms}")
//    String keycloakAdminUrl;
//
//    @Value("${kc.admin.users}")
//    String keycloakAdminUserUrl;
//
//    @Value("${kc.logout}")
//    String keycloakLogout;
//
//    @Value("${kc.user-info-uri}")
//    String keycloakUserInfo;
//
//    @Value("${keycloak.realm}")
//    String realm;
//
//    @Value("${keycloak.resource}")
//    String clientId;
//
//    @Value("${keycloak.credentials.secret}")
//    String clientSecret;
//
//    @Value("${group.garage.id}")
//    String groupGarageId;
//
//    @Value("${group.employee.id}")
//    String groupEmployeeId;
//
//    @Value("${default.password}")
//    String defaultPassword;
//
//    @Autowired
//    KeycloakProvider keycloakProvider;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    public String createUserKeycloak(String username, String name, String userType) {
//        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
//        CredentialRepresentation credentialRepresentation = KeycloakUtil.createPasswordCredentials(defaultPassword);
//
//        UserRepresentation kcUser = new UserRepresentation();
//        kcUser.setUsername(username);
//        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
//        kcUser.setLastName(name);
//        kcUser.setEnabled(false);
//
//        Response response = usersResource.create(kcUser);
//        UserRepresentation userRepresentation = usersResource.search(kcUser.getUsername()).get(0);
//        if (userType.equals("GARAGE_OWNER")) {
//            usersResource.get(userRepresentation.getId()).joinGroup(groupGarageId);
//        }
//        if (userType.equals("GARAGE_EMPLOYEE")) {
//            usersResource.get(userRepresentation.getId()).joinGroup(groupEmployeeId);
//        }
//        return userRepresentation.getId();
////        return "";
//    }
//
//    public void enableUserKeycloak(String keycloakId) {
//        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
//        UserRepresentation kcUser = usersResource.get(keycloakId).toRepresentation();
//        kcUser.setEnabled(true);
//        usersResource.get(keycloakId).update(kcUser);
//    }
//    public void disableUserKeycloak(String keycloakId) {
//        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
//        UserRepresentation kcUser = usersResource.get(keycloakId).toRepresentation();
//        kcUser.setEnabled(false);
//        usersResource.get(keycloakId).update(kcUser);
//    }
//
//    public void logout(String refreshToken) throws Exception {
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("client_id", clientId);
//        map.add("client_secret", clientSecret);
//        map.add("refresh_token", refreshToken);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
//        restTemplate.postForObject(keycloakLogout, request, String.class);
//    }
//
//    private String getUserInfo(String token) {
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.add("Authorization", token);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
//        return restTemplate.postForObject(keycloakUserInfo, request, String.class);
//    }
//
//}
//
