//package com.ac.garage_service.services;
//
//import com.ac.garage_service.entities.AccessToken;
//import com.ac.garage_service.repositories.AccessTokenRepository;
//import com.ac.garage_service.repositories.RoleRepository;
//import com.ac.garage_service.models.requests.UserLoginRequest;
//import com.ac.garage_service.models.responses.LoginUserInfo;
//import com.ac.garage_service.models.responses.UserInfo;
//import com.ac.garage_service.repositories.UserRepository;
//import com.ac.garage_service.securities.CustomUserDetail;
//import com.ac.garage_service.utils.RestApiUtils;
//import model.BaseResponse;
//import model.LoginResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class LoginService extends BaseService {
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Value("${ac.garage.service.token.expire.time}")
//    private int tokenExpireTime;
//
//    @Autowired
//    private AccessTokenRepository accessTokenRepository;
//
//    @Transactional
//    public LoginResponse login(UserLoginRequest request) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = UUID.randomUUID().toString();
//
//        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
//
//        AccessToken accessToken = new AccessToken();
//        accessToken.setToken(token);
//        accessToken.setUserId(userDetails.getId());
//        accessToken.setUsername(userDetails.getUsername());
//        accessToken.setGarageId(userDetails.getGarageId());
//        accessToken.setExpireTime(System.currentTimeMillis() + tokenExpireTime);
//        accessTokenRepository.save(accessToken);
//
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        LoginResponse response = new LoginResponse();
//        response.setCodeSuccess();
//        LoginUserInfo loginUserInfo = new LoginUserInfo();
//        loginUserInfo.setExpiredTime(tokenExpireTime);
//        loginUserInfo.setToken(token);
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserId(userDetails.getId());
//        userInfo.setUsername(userDetails.getUsername());
//        userInfo.setEmail(userDetails.getEmail());
//        userInfo.setRoles(roles);
//        loginUserInfo.setUserInfo(userInfo);
//
//        response.setData(loginUserInfo);
//
//        return response;
//    }
//
//    public BaseResponse logout(HttpServletRequest httpServletRequest) {
//        BaseResponse response = new BaseResponse();
//        String token = RestApiUtils.getTokenFromHeader(httpServletRequest);
//        if (token == null) return response;
//        accessTokenRepository.deleteById(token);
//        return response;
//    }
//}
