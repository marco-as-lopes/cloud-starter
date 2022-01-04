package org.camunda.msl.dto;

public class ClusterStatus {
    private String ready;
    private String zeebeStatus;
    private String operateStatus;
    private String tasklistStatus;
    private String optimizeStatus;

    public ClusterStatus() {
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getZeebeStatus() {
        return zeebeStatus;
    }

    public void setZeebeStatus(String zeebeStatus) {
        this.zeebeStatus = zeebeStatus;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

    public String getTasklistStatus() {
        return tasklistStatus;
    }

    public void setTasklistStatus(String tasklistStatus) {
        this.tasklistStatus = tasklistStatus;
    }

    public String getOptimizeStatus() {
        return optimizeStatus;
    }

    public void setOptimizeStatus(String optimizeStatus) {
        this.optimizeStatus = optimizeStatus;
    }
}
