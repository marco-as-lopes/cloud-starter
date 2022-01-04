package org.camunda.msl.dto;

public class ClusterLinks {
    private String zeebe;
    private String operate;
    private String tasklist;

    public ClusterLinks() {
    }

    public String getZeebe() {
        return zeebe;
    }

    public void setZeebe(String zeebe) {
        this.zeebe = zeebe;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getTasklist() {
        return tasklist;
    }

    public void setTasklist(String tasklist) {
        this.tasklist = tasklist;
    }
}
