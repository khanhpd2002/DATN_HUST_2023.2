package vn.com.cardoctor.garage_service.models.requests.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangePasswordRequest {
    private String oldPassword;
    @NotBlank
    private String newPassword;
    private String retypeNewPassword;
}
