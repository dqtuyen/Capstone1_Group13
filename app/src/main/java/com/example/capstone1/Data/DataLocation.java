package com.example.capstone1.Data;

public class DataLocation {
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName_address() {
        return name_address;
    }

    public void setName_address(String name_address) {
        this.name_address = name_address;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    double latitude;
    double longitude;
    String name_address;
    String UID;

    public DataLocation(double latitude, double longitude, String name_address, String UID) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name_address = name_address;
        this.UID = UID;
    }
}
