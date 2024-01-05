package com.example.capstone1.Data;

public class DataCallingForRescue {
    String name;
    String time;
    String phone;
    String description;
    String address;
    String latitude;
    String longitude;
    String uidCustomer;
    String uidRescue;
    String img;
    public DataCallingForRescue(String name, String phone, String time, String description, String address, String latitude, String longitude,String img, String uidCustomer, String uidRescue) {
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uidCustomer = uidCustomer;
        this.uidRescue = uidRescue;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUidCustomer() {
        return uidCustomer;
    }

    public void setUidCustomer(String uidCustomer) {
        this.uidCustomer = uidCustomer;
    }

    public String getUidRescue() {
        return uidRescue;
    }

    public void setUidRescue(String uidRescue) {
        this.uidRescue = uidRescue;
    }
}
