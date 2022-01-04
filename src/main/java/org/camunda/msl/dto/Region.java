package org.camunda.msl.dto;

class Region {
    public String uuid;
    public String name;
    Region(){}

    @Override
    public String toString() {
        return name+" "+uuid;
    }
}
