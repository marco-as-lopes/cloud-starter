package org.camunda.msl.delegate;

import camundajar.impl.com.google.gson.Gson;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.msl.dto.CreateZeebeClientResponse;
import org.camunda.msl.helper.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

@Component("DeleteClusterDelegate")
public class DeleteClusterDelegate implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Autowired
    private Authentication authentication;

    @Value("${zeebe.client.cloud.zeebeAPI}")
    private String zeebeAPI;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("DeleteClusterDelegate - IN - " + new Date());

        String clusterId = (String) execution.getVariable("clusterId");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+authentication.getToken());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Object> resp = restTemplate.exchange(zeebeAPI+"/clusters/"+clusterId, HttpMethod.DELETE, entity, Object.class);

        LOGGER.info("DeleteClusterDelegate - OUT");
    }
}