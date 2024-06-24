package vn.com.cardoctor.garage_service.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import model.BaseResponse;
import org.springframework.web.bind.annotation.*;
import utils.ApiException;
import vn.com.cardoctor.garage_service.models.requests.users.ChangePasswordRequest;
import vn.com.cardoctor.garage_service.models.requests.users.UserLoginRequest;
import vn.com.cardoctor.garage_service.models.responses.UserInfo;
import vn.com.cardoctor.garage_service.models.responses.users.AccessTokenResponse;
import vn.com.cardoctor.garage_service.services.users.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {
//    private final UserServiceGrpc userServiceGrpc;

    private final UserService userService;


    @PostMapping("/login")
    public BaseResponse<AccessTokenResponse> login(@NotNull @RequestBody UserLoginRequest loginRequest) {
        BaseResponse<AccessTokenResponse> response = new BaseResponse<>();
        AccessTokenResponse accessTokenResponse;
        try {
            accessTokenResponse = userService.login(loginRequest);
            response.setData(accessTokenResponse);
        } catch (ApiException ex) {
            log.warn("invalid account. User probably hasn't verified email.", ex);
            response.setCode(401);
            response.setMessage("Invalid credentials");
            response.setData(null);
        }
        return response;
    }

    @GetMapping("/me")
    public BaseResponse<UserInfo> getUserInfo(HttpServletRequest httpServletRequest) throws ApiException {
        BaseResponse<UserInfo> response = new BaseResponse<>();
        UserInfo userInfo = this.userService.getUserInfo(httpServletRequest);
        response.setData(userInfo);
        return response;
    }

    @PostMapping("/change-password")
    public BaseResponse<String> getCurrentPassword(@RequestBody @NotNull ChangePasswordRequest changePasswordRequest) throws ApiException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(this.userService.changePassword(changePasswordRequest));
        return response;
    }

}
