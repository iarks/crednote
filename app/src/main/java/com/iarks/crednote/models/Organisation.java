package com.iarks.crednote.models;

public class Organisation {
    private final String name;
    private final String[] address;
    private final String gstin;
    private final String state;
    private final int stateCode;

    public Organisation(String name, String[] address, String gstin, String state, int stateCode) {
        this.name = name;
        this.address = address;
        this.gstin = gstin;
        this.state = state;
        this.stateCode = stateCode;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return String.join("\n", address);
    }

    public String getGSTIn(){
        return gstin;
    }

    public String getState(){
        return state;
    }

    public int getStateCode(){
        return stateCode;
    }
}
