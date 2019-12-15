package com.example.sharecar.DataSet;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Write implements Serializable {
    private String key;
    private String name;
    private String age;
    private double startLatitude;
    private double destLatitude;
    private double startLongitude;
    private double destLongitude;
    private String uptDate;
    private String departTime;


    public Write(){}
    public Write(
            String key,
            String name,
            String age,
            double startLatitude,
            double destLatitude,
            double startLongitude,
            double destLongitude,
            String uptDate,
            String departTime
    ){
        this.key = key;
        this.name = name;
        this.age = age;
        this.startLatitude=startLatitude;
        this.destLatitude=destLatitude;
        this.startLongitude=startLongitude;
        this.destLongitude=destLongitude;
        this.uptDate=uptDate;
        this.departTime=departTime;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public String getUptDate() {
        return uptDate;
    }

    public void setUptDate(String uptDate) {
        this.uptDate = uptDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
