package org.camunda.msl.helper;

import camundajar.impl.com.google.gson.Gson;
import org.camunda.msl.delegate.LoggerDelegate;
import org.camunda.msl.dto.Token;
import org.camunda.msl.dto.TokenRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class Authentication {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Value("${zeebe.client.cloud.zeebeAPI}")
    private String zeebeAPI;
    @Value("${zeebe.client.cloud.oAuthAPI}")
    private String oAuthAPI;
    @Value("${zeebe.client.cloud.clientId}")
    private String clientId;
    @Value("${zeebe.client.cloud.clientSecret}")
    private String clientSecret;

    public String getToken() throws Exception
    {
        LOGGER.info("GetToken - IN - "+ new Date());

        TokenRequest tr = new TokenRequest("client_credentials","api.cloud.camunda.io",clientId,clientSecret );
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(tr), headers);
        ResponseEntity<Token> resp = restTemplate.exchange(oAuthAPI+"/oauth/token", HttpMethod.POST, entity, Token.class);

        LOGGER.info("GetToken - OUT");
        return resp.getBody().getAccess_token();
    }
}
