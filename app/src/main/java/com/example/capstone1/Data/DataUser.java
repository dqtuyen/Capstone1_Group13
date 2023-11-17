package com.example.capstone1.Data;

public class DataUser {
    private String UID;
    private String name;
    private String call;
    private String email;
    private String address;
    private String decription;
    private String time;
    private String num_car;
    private String type_car;
    private String rank;

    private String img;
    public DataUser(String UID, String name, String call, String email, String address, String turns,String time, String num_car, String type_car, String rank, String img) {
        this.UID = UID;
        this.name = name;
        this.call = call;
        this.email = email;
        this.address = address;
        this.decription = turns;
        this.time = time;
        this.num_car = num_car;
        this.type_car = type_car;
        this.rank = rank;
        this.img = img;
    }

    public DataUser(String UID, String name, String call, String address, String decription, String time, String num_car, String type_car, String img) {
        this.UID = UID;
        this.name = name;
        this.call = call;
        this.address = address;
        this.decription = decription;
        this.time = time;
        this.num_car = num_car;
        this.type_car = type_car;
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getNum_car() {
        return num_car;
    }

    public void setNum_car(String num_car) {
        this.num_car = num_car;
    }

    public String getType_car() {
        return type_car;
    }

    public void setType_car(String type_car) {
        this.type_car = type_car;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
