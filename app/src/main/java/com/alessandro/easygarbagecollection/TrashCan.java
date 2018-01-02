package com.alessandro.easygarbagecollection;

/**
 * Created by alessandro on 20/11/2017.
 * TrashCan model
 */

public class TrashCan {

    public String code;
    public double fillingLevel;
    public double latitude;
    public double longitude;
    public String lastUpdate;


    public TrashCan(String code, double fillingLevel, double latitude, double longitude, String lastUpdate) {
        this.code = code;
        this.fillingLevel = fillingLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdate = lastUpdate;
    }

    public TrashCan() {
    }

    String getCode() {
        return code;
    }


    double getFillingLevel() {
        return fillingLevel;
    }


    double getLongitude() {
        return longitude;
    }


    double getLatitude() {
        return latitude;
    }

    String getLastUpdate() { return lastUpdate; }
}