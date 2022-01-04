package org.camunda.msl.dto;

public class CreateZeebeClientRequest {
    private String clientName;
    public CreateZeebeClientRequest(String clientName)
    {
        this.clientName = clientName;
    }
}
