package org.camunda.msl.dto;

import java.util.Arrays;

public class ClusterCreationParametersResponse {
    private Channel[] channels;
    private ClusterPlanType[] clusterPlanTypes;
    private Region[] regions;
    ClusterCreationParametersResponse(){}

    public String getChannels(){
        String toReturn = "";
        for (Channel c : channels) {
            toReturn += c.name + " * " + c.uuid +" $ ";
        }
        return toReturn;
    }

    public String getGenerations(){
        String toReturn = "";
        for (Channel c : channels) {
            toReturn += "D - " + c.defaultGeneration.name + " * " + c.defaultGeneration.uuid +" $ ";
            for (Generation g : c.allowedGenerations) {
                toReturn += "A - " + g.name + " * " + g.uuid +" $ ";
            }
        }
        return toReturn;
    }

    public String getClusterPlanTypes(){
        String toReturn = "";
        for (ClusterPlanType c : clusterPlanTypes) {
            toReturn += c.name + " * " + c.uuid +" $ ";
        }
        return  toReturn;
    }

    public String getRegions(){
        String toReturn = "";
        for (Region r : regions) {
            toReturn += r.name + " * " + r.uuid +" $ ";
        }
        return  toReturn;
    }

}
