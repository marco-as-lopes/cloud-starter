package org.camunda.msl.dto;

public class Channel {
    public String uuid;
    public String name;
    public Generation defaultGeneration;
    public Generation[] allowedGenerations;
    Channel(){}

    @Override
    public String toString() {
        String toString = name+" "+uuid+"\r\n"+"Default Generation "+defaultGeneration+"\r\n"+"Allowed Generations"+"\r\n";

        for (Generation g : allowedGenerations) {
            toString += g.toString()+"\r\n";
        }

        return toString;
    }
}
