package com.example.models;

import com.google.android.gms.maps.model.LatLng;

public class Dustbin {

    private int dustbinID;
    private String dustbinName;
    private double longitude;
    private double latitude;
    private double fillLevel;

    public Dustbin() {}

    public Dustbin(int dustbinID, String dustbinName, double longitude, double latitude, double fillLevel) {
        this.dustbinID = dustbinID;
        this.dustbinName = dustbinName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.fillLevel = fillLevel;
    }

    public int getDustbinID() {
        return dustbinID;
    }

    public void setDustbinID(int dustbinID) {
        this.dustbinID = dustbinID;
    }

    public String getDustbinName() {
        return dustbinName;
    }

    public void setDustbinName(String dustbinName) {
        this.dustbinName = dustbinName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getFillLevel() {
        return fillLevel;
    }

    public void setFillLevel(double fillLevel) {
        this.fillLevel = fillLevel;
    }

    @Override
    public String toString() {
        return Integer.toString(dustbinID);
    }


}
