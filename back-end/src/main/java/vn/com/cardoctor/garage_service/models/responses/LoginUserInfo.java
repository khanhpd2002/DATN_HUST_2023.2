package vn.com.cardoctor.garage_service.models.responses;

import lombok.Data;
import model.LoginResponse;


@Data
public class LoginUserInfo extends LoginResponse.LoginData {
    private UserInfo userInfo;
}
