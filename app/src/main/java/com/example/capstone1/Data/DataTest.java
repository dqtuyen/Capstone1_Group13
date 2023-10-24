package com.example.capstone1.Data;

public class DataTest {
    private String name;
    private String call;
    private String email;
    private String address;
    private String turns;
    private String num_car;
    private String type_car;
    private String rank;

    private int img;
    public DataTest(String name, String call, String email, String address, String turns, String num_car, String type_car, String rank, int img) {
        this.name = name;
        this.call = call;
        this.email = email;
        this.address = address;
        this.turns = turns;
        this.num_car = num_car;
        this.type_car = type_car;
        this.rank = rank;
        this.img = img;
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

    public String getTurns() {
        return turns;
    }

    public void setTurns(String turns) {
        this.turns = turns;
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
