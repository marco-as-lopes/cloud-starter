package org.camunda.msl.dto;

public class ClusterInfo {
    private String uuid;
    private String name;
    private String created;
    private K8sContext k8sContext;
    private Generation generation;
    private Channel channel;
    private ClusterStatus status;
    private ClusterLinks links;

    public ClusterInfo() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public K8sContext getK8sContext() {
        return k8sContext;
    }

    public void setK8sContext(K8sContext k8sContext) {
        this.k8sContext = k8sContext;
    }

    public Generation getGeneration() {
        return generation;
    }

    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ClusterStatus getStatus() {
        return status;
    }

    public void setStatus(ClusterStatus status) {
        this.status = status;
    }

    public ClusterLinks getLinks() {
        return links;
    }

    public void setLinks(ClusterLinks links) {
        this.links = links;
    }
}
