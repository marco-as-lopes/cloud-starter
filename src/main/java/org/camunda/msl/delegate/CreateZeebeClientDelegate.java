package org.camunda.msl.delegate;


import camundajar.impl.com.google.gson.Gson;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.msl.dto.ClusterCreation;
import org.camunda.msl.dto.CreateZeebeClientRequest;
import org.camunda.msl.dto.CreateZeebeClientResponse;
import org.camunda.msl.helper.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

@Component("CreateZeebeClientDelegate")
public class CreateZeebeClientDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Autowired
    private Authentication authentication;

    @Value("${zeebe.client.cloud.zeebeAPI}")
    private String zeebeAPI;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("CreateZeebeClientDelegate - IN - "+ new Date());

        String clusterId = (String) execution.getVariable("clusterId");
        String clientName =  (String) execution.getVariable("clientName");

        CreateZeebeClientRequest czcr = new CreateZeebeClientRequest(clientName);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+authentication.getToken());
        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(czcr), headers);
        ResponseEntity<CreateZeebeClientResponse> resp = restTemplate.exchange(zeebeAPI+"/clusters/"+clusterId+"/clients", HttpMethod.POST, entity, CreateZeebeClientResponse.class);

        execution.setVariable("clientId" , resp.getBody().getClientId());
        execution.setVariable("clientSecret" , resp.getBody().getClientSecret());
        execution.setVariable("name" , resp.getBody().getName());

        LOGGER.info("CreateZeebeClientDelegate - OUT");
    }
}
