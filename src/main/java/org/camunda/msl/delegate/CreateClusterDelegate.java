package org.camunda.msl.delegate;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.JsonObject;
import camundajar.impl.com.google.gson.JsonParser;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.msl.ProcessConstants;
import org.camunda.msl.dto.ClusterCreation;
import org.camunda.msl.dto.ClusterCreationParametersResponse;
import org.camunda.msl.dto.ClusterInfo;
import org.camunda.msl.dto.CreateClusterRequest;
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

@Component("CreateClusterDelegate")
public class CreateClusterDelegate implements JavaDelegate {

    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

    @Autowired
    private Authentication authentication;

    @Value("${zeebe.client.cloud.zeebeAPI}")
    private String zeebeAPI;

    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("CreateClusterDelegate - IN - "+ new Date());

        String name = (String) execution.getVariable("clusterName");
        String channelId = (String) execution.getVariable("channelId");
        String generationId = (String) execution.getVariable("generationId");
        String regionId = (String) execution.getVariable("regionId");
        String planTypeId = (String) execution.getVariable("planTypeId");

        CreateClusterRequest ccr = new CreateClusterRequest(name,channelId,generationId,regionId,planTypeId);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+authentication.getToken());
        HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(ccr), headers);
        ResponseEntity<ClusterCreation> resp = restTemplate.exchange(zeebeAPI+"/clusters", HttpMethod.POST, entity, ClusterCreation.class);
        execution.setVariable("clusterId", resp.getBody().getClusterId());

        LOGGER.info("Cluster in creation - Starting wait cycle!");

        Thread.sleep(5000);

        long sleepTime = 0;
        ResponseEntity<ClusterInfo> ci = null;
        restTemplate = new RestTemplate();
        entity = new HttpEntity<>("", headers);

        while (true) {
            ci = restTemplate.exchange(zeebeAPI+"/clusters/"+resp.getBody().getClusterId(), HttpMethod.GET, entity, ClusterInfo.class);

            if(ci.getBody().getStatus().getReady().equals("Healthy"))
                break;

            sleepTime = sleepTime + ProcessConstants.WAIT_TIME;
            LOGGER.info("Cluster not ready - sleep : " + sleepTime + " ms");
            Thread.sleep(sleepTime);
        }

        execution.setVariable("zeebeLink", ci.getBody().getLinks().getZeebe());
        execution.setVariable("operateLink", ci.getBody().getLinks().getOperate());
        execution.setVariable("tasklistLink", ci.getBody().getLinks().getTasklist());
        execution.setVariable("region", ci.getBody().getLinks().getZeebe().split("\\.")[1]);

        LOGGER.info("Cluster ready : " + sleepTime + "ms");
        LOGGER.info("CreateClusterDelegate - OUT");
    }
}
