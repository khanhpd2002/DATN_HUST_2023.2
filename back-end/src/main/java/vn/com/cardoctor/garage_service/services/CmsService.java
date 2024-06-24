package vn.com.cardoctor.garage_service.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.com.cardoctor.garage_service.entities.rest.CmsAuthTokenResponse;
import vn.com.cardoctor.garage_service.entities.rest.ExpertRestEntity;
import vn.com.cardoctor.garage_service.entities.rest.OperatorRestEntity;
import vn.com.cardoctor.garage_service.entities.rest.RestPageImpl;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CmsService<T> {
    @Value("${client.clientId}")
    private String clientId;

    @Value("${client.clientSecret}")
    private String clientSecret;

    @Value("${client.grant-type}")
    private String grantType;

    @Value("${single.cms.domain.on.url}")
    private String cmsDomain;

    @Value("${single.sso.domain.on.url}")
    private String ssoDomain;

    @Value("${client.username}")
    private String username;

    @Value("${client.password}")
    private String password;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;
    public HttpEntity<MultiValueMap<String, String>> buildAuthHeader() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("username", username);
        map.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        String authStr = clientId + ":" + clientSecret;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());
        headers.set("Authorization", "Basic " + base64Creds);

        return new HttpEntity<>(map, headers);
    }

    public HttpEntity<String> buildAuthHeader(String token) {
        HttpHeaders auth = new HttpHeaders();
        auth.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(auth);
    }

    public ResponseEntity<CmsAuthTokenResponse> getToken() {
        return this.restTemplate.exchange(ssoDomain, HttpMethod.POST,
                this.buildAuthHeader(), CmsAuthTokenResponse.class);
    }

    public ResponseEntity<RestPageImpl<T>> getListCmsApi(String path, String name, Integer pageNo, Integer pageSize,
                                                         String code, String status, String type) {

        ResponseEntity<RestPageImpl<T>> restPageResponseEntity = null;
        String token = this.getToken().getBody().getAccessToken();
        // Get paging by rest template
        ParameterizedTypeReference<RestPageImpl<T>> responseType = new ParameterizedTypeReference<RestPageImpl<T>>() {
        };
        restPageResponseEntity = this.restTemplate.exchange(cmsDomain + "/" + path + "/"
                + name + "?" + "pageNumber="+ pageNo + "&" + "pageSize=" + pageSize + "&code=" + code
                + "&status=" + status + "&type=" + type, HttpMethod.GET, this.buildAuthHeader(token), responseType);
        return restPageResponseEntity;
    }

    public List<Long> getAllExpertIdsOfCms() {
        List<ExpertRestEntity> expertRestEntityOutput;
        List<Long> expertIds = null;
        String accessToken = this.getToken().getBody().getAccessToken();
        if (accessToken != null) {
            RestPageImpl<ExpertRestEntity> expertRestEntities = (RestPageImpl<ExpertRestEntity>) this
                    .getListCmsApi("cms", "expert", 1, 100000, null,
                            "ACCEPTED", null)
                    .getBody();
            if (expertRestEntities == null) {
                return Collections.emptyList();
            }
            List<ExpertRestEntity> expertList = Objects.requireNonNull(expertRestEntities.getContent());
            expertRestEntityOutput = this.objectMapper.convertValue(expertList, new TypeReference<List<ExpertRestEntity>>() {
            });
            expertIds = expertRestEntityOutput.stream().map(ExpertRestEntity::getId).collect(Collectors.toList());
        }
        return expertIds;
    }

    public List<Long> getAllOperatorIdsOfCms() {
        List<OperatorRestEntity> operatorRestEntityOutput;
        List<Long> operatorIds = null;
        String accessToken = this.getToken().getBody().getAccessToken();
        if (accessToken != null) {
            RestPageImpl<OperatorRestEntity> operatorRestEntities = (RestPageImpl<OperatorRestEntity>) this
                    .getListCmsApi("cms", "operator", 1, 100000, null,
                            "ACCEPTED", null)
                    .getBody();
            if (operatorRestEntities == null) {
                return Collections.emptyList();
            }
            List<OperatorRestEntity> expertList = Objects.requireNonNull(operatorRestEntities.getContent());
            operatorRestEntityOutput = this.objectMapper.convertValue(expertList, new TypeReference<List<OperatorRestEntity>>() {
            });
            operatorIds = operatorRestEntityOutput.stream().map(OperatorRestEntity::getId).collect(Collectors.toList());
        }
        return operatorIds;
    }
}
