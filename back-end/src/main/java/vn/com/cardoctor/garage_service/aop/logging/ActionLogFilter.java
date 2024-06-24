package vn.com.cardoctor.garage_service.aop.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import vn.com.cardoctor.garage_service.entities.Log;
import vn.com.cardoctor.garage_service.models.LogAdditionalInformation;
import vn.com.cardoctor.garage_service.models.responses.ActionLogResponse;
import vn.com.cardoctor.garage_service.repositories.LogRepository;
import vn.com.cardoctor.garage_service.utils.DataUtils;
import vn.com.cardoctor.garage_service.utils.StringPool;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Order(100)
@Log4j2
@RequiredArgsConstructor
public class ActionLogFilter extends OncePerRequestFilter {
    private final LogRepository logRepository;

    private final List<String> blackList = List.of(
            "\\/api\\/certificate\\/.well-known\\/jwks\\.json",
            ".*\\/actuator\\/.*",
            "/swagger-ui.*",
            "/swagger-resources.*",
            "/v2/api-docs.*");
    private final List<String> blackListMimeType = List.of(
            "multipart\\/form-data.*",
            "image\\/.*",
            "application\\/octet-stream.*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
        cachedResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ActionLogResponse data = new ActionLogResponse();
        String requestContentType = request.getHeader(StringPool.HEADER_CONTENT_TYPE);
        boolean ignoredRequest = inBlackListUrl(request) ||
                (!DataUtils.isNullOrEmpty(requestContentType) && inBlackListMimeType(requestContentType));
        if (!ignoredRequest) {
            request = new CachedHttpServletRequestWrapper(request);
            loggingRequest(request, data);
        }
        try {
            filterChain.doFilter(request, cachedResponse);
        } finally {
            byte[] responseArray = cachedResponse.getContentAsByteArray();
            cachedResponse.copyBodyToResponse();
            String responseContentType = cachedResponse.getHeader(StringPool.HEADER_CONTENT_TYPE);
            boolean ignoredResponse = inBlackListUrl(request) ||
                    (!DataUtils.isNullOrEmpty(responseContentType) && inBlackListMimeType(responseContentType));
            if (!ignoredResponse) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                loggingResponse(cachedResponse, new String(responseArray, cachedResponse.getCharacterEncoding()), data, elapsedTime);
            }
        }
    }

    private void loggingRequest(HttpServletRequest request, ActionLogResponse data) {
        log.info("=========================== REQUEST RESOURCE START ================================================");
        String method = request.getMethod();
        data.setUri(request.getRequestURI());
        data.setMethod(method);
        data.setRemoteIp(this.getRemoteIp(request));
        data.setHostIp(request.getLocalAddr());
        data.setRequestParams(request.getQueryString());
        if (request instanceof CachedHttpServletRequestWrapper
                && !StringPool.GET_METHOD.equalsIgnoreCase(method)) {
            String body = ((CachedHttpServletRequestWrapper) request).getBody();
            data.setRequestPayload(body);
        }
    }

    private void loggingResponse(ContentCachingResponseWrapper cacheResponse, String responseStr, ActionLogResponse data, long elapsedTime) {
        data.setStatus(cacheResponse.getStatus());
        data.setResponsePayload(responseStr);
        log.info("CONTENT: {} ", data);
        log.info("=========================== REQUEST RESOURCE END ================================================");
        Log log = new Log();
        log.setParams(data.getRequestParams());
        log.setRequest(data.getRequestPayload());
        log.setResponse(data.getResponsePayload());
        LogAdditionalInformation logAdditionalInformation = new LogAdditionalInformation();
        logAdditionalInformation.setUri(data.getUri());
        logAdditionalInformation.setMethod(data.getMethod());
        logAdditionalInformation.setRemoteIp(data.getRemoteIp());
        logAdditionalInformation.setHostIp(data.getHostIp());
        logAdditionalInformation.setTime(elapsedTime);
        log.setAdditionalInformation(new Gson().toJson(logAdditionalInformation));
        Thread thread = new Thread(() -> this.logRepository.save(log));
        thread.start();
    }

    private String replaceRequestBody(String body) {
        if (DataUtils.isNullOrEmpty(body)) {
            return body;
        }
        return body.replaceAll("\"password\":\"(.*?)\"", "\"password\":\"******\"")
                .replaceAll("\"clientSecret\":\"(.*?)\"", "\"clientSecret\":\"******\"");
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader(StringPool.X_FORWARDED_FOR);
        if (!DataUtils.isNullOrEmpty(ip) && !StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
            int index = ip.indexOf(StringPool.COMMA);
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader(StringPool.X_REAL_IP);
        if (!DataUtils.isNullOrEmpty(ip) && !StringPool.UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private boolean inBlackListUrl(HttpServletRequest request) {
//        check isBlack list
        if (this.blackList.isEmpty()) {
            return Boolean.FALSE;
        }
        String uri = String.valueOf(request.getRequestURI());
        return this.blackList.stream().anyMatch(uri::matches);
    }

    private boolean inBlackListMimeType(String responseContentType) {
        if (DataUtils.isNullOrEmpty(this.blackListMimeType)) {
            return Boolean.FALSE;
        }
        return this.blackListMimeType.stream().anyMatch(responseContentType::matches);
    }

}

