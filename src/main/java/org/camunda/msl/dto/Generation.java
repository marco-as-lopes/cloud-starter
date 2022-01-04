package org.camunda.msl.dto;

public class Generation {
    public String uuid;
    public String name;
    Generation(){}

    @Override
    public String toString() {
        return name+" "+uuid;
    }
}
