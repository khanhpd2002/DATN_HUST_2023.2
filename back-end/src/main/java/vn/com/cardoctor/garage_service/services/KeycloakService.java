package vn.com.cardoctor.garage_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utils.ApiException;
import utils.ERROR;
import vn.com.cardoctor.garage_service.configs.KeycloakProvider;
import vn.com.cardoctor.garage_service.models.requests.users.ChangePasswordRequest;

import javax.ws.rs.NotAuthorizedException;

@Service
@Log4j2
@RequiredArgsConstructor
public class KeycloakService extends BaseService {
    @Value("${kc.admin.realms}")
    String keycloakAdminUrl;

    @Value("${kc.admin.users}")
    String keycloakAdminUserUrl;

    @Value("${kc.logout}")
    String keycloakLogout;

    @Value("${kc.user-info-uri}")
    String keycloakUserInfo;

    @Value("${keycloak.realm}")
    String realm;

    @Value("${keycloak.resource}")
    String clientId;

    @Value("${keycloak.credentials.secret}")
    String clientSecret;

    @Value("${group.garage.id}")
    String groupGarageId;

    @Value("${group.employee.id}")
    String groupEmployeeId;

    @Value("${default.password}")
    String defaultPassword;

    private final KeycloakProvider keycloakProvider;

    public void enableUserKeycloak(String keycloakId) {
        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
        UserRepresentation kcUser = usersResource.get(keycloakId).toRepresentation();
        kcUser.setEnabled(true);
        usersResource.get(keycloakId).update(kcUser);
    }
    public void disableUserKeycloak(String keycloakId) {
        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
        UserRepresentation kcUser = usersResource.get(keycloakId).toRepresentation();
        kcUser.setEnabled(false);
        usersResource.get(keycloakId).update(kcUser);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) throws ApiException {
        String keycloakId;
        keycloakId = this.getKeyCloakUserId();
        // check old password by login
        this.checkOldPassword(keycloakId, changePasswordRequest.getOldPassword());
        this.validateOldPasswordAndNewPassword(changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        this.resetPassword(keycloakId, changePasswordRequest);
        this.logout();
    }

    public void checkOldPassword(String keycloakId, String oldPassword) throws ApiException {
        try {
            UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
            UserRepresentation kcUser = usersResource.get(keycloakId).toRepresentation();
            keycloakProvider.newKeycloakBuilderWithPasswordCredentials(kcUser.getUsername(), oldPassword).build()
                    .tokenManager().getAccessToken();
        } catch (NotAuthorizedException ex) {
            throw new ApiException(ERROR.BAD_REQUEST, "Mật khẩu cũ không chính xác.");
        }
    }

    public void validateOldPasswordAndNewPassword(String oldPassword, String newPassword) throws ApiException {
        if (oldPassword.equals(newPassword)) {
            throw new ApiException(ERROR.BAD_REQUEST, "Mật khẩu mới trùng với Mật khẩu cũ");
        }
    }

    public void resetPassword(String keycloakId, ChangePasswordRequest changePasswordRequest) {
        CredentialRepresentation newCredential = doSetAccountCredential(changePasswordRequest.getNewPassword());
        this.doResetPassword(keycloakId, newCredential);
    }

    public CredentialRepresentation doSetAccountCredential(String password) {
        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setTemporary(false);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(password);
        return newCredential;
    }

    public void doResetPassword(String keycloakId, CredentialRepresentation newCredential) {
        try (Keycloak keycloak = keycloakProvider.getInstance()) {
            UsersResource usersResource = keycloak.realm(realm).users();
            usersResource.get(keycloakId).resetPassword(newCredential);
        } catch (Exception e) {
            log.error("Failed to reset password with keycloak Id: {}", keycloakId);
        }
    }

    public void logout() throws ApiException {
        Keycloak keycloak = keycloakProvider.getInstance();
        try {
            UsersResource usersResource = keycloak.realm(realm).users();
            usersResource.get(this.getKeyCloakUserId()).logout();
        } catch (Exception e) {
            throw new ApiException(403, "Không có quyền truy cập");
        }
    }
}

