package org.camunda.msl.dto;

public class ClusterPlanType {
    public String uuid;
    public String name;
    private K8sContext k8sContext;
    ClusterPlanType(){}

    @Override
    public String toString() {
        return name+" "+uuid;
    }
}
