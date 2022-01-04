package org.camunda.msl.helper;

import io.camunda.zeebe.client.ZeebeClient;

public class ZeebeClientFactory {
    public static ZeebeClient getZeebeClient(String ClusterId, String ClientId, String ClientSecret, String region) {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId(ClusterId)
                .withClientId(ClientId)
                .withClientSecret(ClientSecret)
                .withRegion(region)
                .build();
    }
}
