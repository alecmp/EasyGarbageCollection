package com.alessandro.easygarbagecollection;

/**
 * Created by alessandro.campanell on 20/11/2017.
 * TrashCan model
 */

public class TrashCan {

    private String code;
    private double fillingLevel;
    private double latitude;
    private double longitude;

    public TrashCan(String code, double fillingLevel, double latitude, double longitude) {
        this.code = code;
        this.fillingLevel = fillingLevel;
        this.latitude = latitude;
        this.longitude = longitude;
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

}