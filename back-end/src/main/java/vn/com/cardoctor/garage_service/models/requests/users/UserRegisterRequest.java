package vn.com.cardoctor.garage_service.models.requests.users;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
}
