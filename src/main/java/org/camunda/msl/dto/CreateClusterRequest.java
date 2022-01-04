package org.camunda.msl.dto;

public class CreateClusterRequest {

    private String name;
    private String channelId;
    private String generationId;
    private String regionId;
    private String planTypeId;

    public CreateClusterRequest(String name, String channelId, String generationId, String regionId, String planTypeId)
    {
        this.name = name;
        this.channelId = channelId;
        this.generationId = generationId;
        this.regionId = regionId;
        this.planTypeId = planTypeId;
    }
}
