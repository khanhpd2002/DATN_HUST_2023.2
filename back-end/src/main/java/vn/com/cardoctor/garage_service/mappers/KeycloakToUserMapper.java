//package vn.com.cardoctor.garage_service.mappers;
//
//import org.keycloak.representations.idm.UserRepresentation;
//import org.springframework.stereotype.Component;
//import vn.com.cardoctor.garage_service.models.responses.UserResponse;
//
//@Component
//public class KeycloakToUserMapper {
//    public UserResponse convertUserKeycloakToUser(UserRepresentation userRepresentation) {
//        UserResponse userResponse = new UserResponse();
//
//        userResponse.setId(userRepresentation.getId());
//        userResponse.setUsername(userRepresentation.getUsername());
//        userResponse.setFirstName(userRepresentation.getFirstName());
//        userResponse.setLastName(userRepresentation.getLastName());
//        userResponse.setEnabled(userRepresentation.isEnabled());
//        userResponse.setAttemptLoginFailed(Integer.valueOf(userRepresentation.getAttributes().get("attemptLoginFailed").get(0)));
//        userResponse.setMustChangePassword(Boolean.valueOf(userRepresentation.getAttributes().get("mustChangePassword").get(0)));
//        userResponse.setSystemRef(userRepresentation.getAttributes().get("systemRef").get(0));
//        userResponse.setSystemRefUserType(userRepresentation.getAttributes().get("systemRefUserType").get(0));
//        userResponse.setSystemRefUserId(Long.valueOf(userRepresentation.getAttributes().get("systemRefUserId").get(0)));
//        userResponse.setIsAcceptSystemPolicy(Boolean.valueOf(userRepresentation.getAttributes().get("isAcceptSystemPolicy").get(0)));
//        userResponse.setAppName(userRepresentation.getAttributes().get("appName").get(0));
//        userResponse.setAppVersion(userRepresentation.getAttributes().get("appVersion").get(0));
//        userResponse.setDeviceType(userRepresentation.getAttributes().get("deviceType").get(0));
//
//        return userResponse;
//    }
//}
//
