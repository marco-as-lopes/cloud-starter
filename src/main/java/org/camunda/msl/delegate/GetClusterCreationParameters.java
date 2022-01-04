package org.camunda.msl.delegate;

import camundajar.impl.com.google.gson.Gson;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.msl.dto.ClusterCreationParametersResponse;
import org.camunda.msl.helper.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Logger;

@Component("GetClusterCreationParameters")
@Service
public class GetClusterCreationParameters implements JavaDelegate {
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Autowired
    private Authentication authentication;

    @Value("${zeebe.client.cloud.zeebeAPI}")
    private String zeebeAPI;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("GetClusterCreationParameters - IN - "+ new Date());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization","Bearer "+authentication.getToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ClusterCreationParametersResponse> resp = restTemplate.exchange(zeebeAPI+"/clusters/parameters", HttpMethod.GET, entity, ClusterCreationParametersResponse.class);

        execution.setVariable("channel" , resp.getBody().getChannels());
        execution.setVariable("generation" , resp.getBody().getGenerations());
        execution.setVariable("region" , resp.getBody().getRegions());
        execution.setVariable("planType" , resp.getBody().getClusterPlanTypes());

        LOGGER.info("GetClusterCreationParameters - OUT");
    }
}
