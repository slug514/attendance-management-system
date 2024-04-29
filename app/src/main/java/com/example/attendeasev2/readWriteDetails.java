package com.example.attendeasev2;

public class readWriteDetails {

    String fname,type;
    public readWriteDetails(String fname,String type)
    {
        this.fname=fname;
        this.type=type;
    }

    public String getFname() {
        return fname;
    }

    public String getType() {
        return type;
    }
}
