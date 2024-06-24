package vn.com.cardoctor.garage_service.aop;

import authentication.AcAuthentication;
import authentication.UserAuthentication;
import authentication.UserInfoContext;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.Map;

@Component
@Aspect
@Log4j2
public class LogAspect {
    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    public static final String APPLICATION_ID = "x-application-id";
    public static final String USER_INFO = "x-userinfo";

    public static final String ENV = "x-environment";

    @Pointcut("execution(* vn.com.cardoctor.*.controllers..*(..)))")
    protected void applicationControllerAllMethod() {
    }

    @Around("(applicationControllerAllMethod()) ")
    public Object logAspectController(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        String ip = getIPRequest(this.httpServletRequest);
        String headers = header(httpServletRequest);
        log.debug("request from ip {} , header {}", ip, headers);
        Object output;
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();

            AcAuthentication authentication = method.getDeclaredAnnotation(AcAuthentication.class);

            if (authentication != null && authentication.enableAuthentication()) {


                UserInfoContext.add(httpServletRequest.getHeader(USER_INFO));

                if (authentication.applyClient().length != 0) {

                    UserAuthentication userAuthentication = UserInfoContext.getUserInfo();
                    boolean isPass = true;
                    for (String applicationId : authentication.applyClient()) {
                        if (applicationId.contains("*")) {
                            break;
                        }

                        if (!applicationId.equals(userAuthentication.getClientId())) {
                            isPass = false;
                            break;
                        }
                    }

                    if (!isPass) {
                        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                        return httpServletResponse;
                    }
                }
            }


        } catch (InvalidParameterException ex) {
            log.error("not get user info ex", ex);
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return null;
        } catch (Exception e) {
            log.error("aop ex", e);
        }


        output = pjp.proceed();

        long elapsedTime = System.currentTimeMillis() - startTime;

        log.debug("[REQ] ip : {} ; Request uri: {} ;method: {} ;params: {} ;header: {} ",
                ip,
                this.httpServletRequest.getRequestURI(),
                this.httpServletRequest.getMethod(),
                getParams(this.httpServletRequest.getParameterMap()),
                headers);

        log.debug(" >>> Exiting method <<< ,time: {} ms", elapsedTime);

        return output;
    }

    private String header(HttpServletRequest httpServletRequest) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> x = httpServletRequest.getHeaderNames();
        while (x.hasMoreElements()) {
            String key = x.nextElement();
            sb.append(key).append("=").append(httpServletRequest.getHeader(key)).append(",");
        }
        return sb.toString();
    }


    private String getIPRequest(HttpServletRequest servletRequest) {
        if (servletRequest == null)
            return null;
        String remoteIPAddress;
        remoteIPAddress = servletRequest.getHeader("X-FORWARDED-FOR");
        if (remoteIPAddress == null || remoteIPAddress.isEmpty()) {
            remoteIPAddress = servletRequest.getRemoteAddr();
        }

        return remoteIPAddress;
    }

    private String getParams(Map<String, String[]> parameterMap) {
        if (parameterMap == null || parameterMap.isEmpty())
            return "No Params";
        StringBuilder sb = new StringBuilder();
        for (String key : parameterMap.keySet()) {
            sb.append(key).append("=").append(parameterMap.get(key)[0]).append(",");
        }
        return sb.toString();
    }
}
