package com.example.capstone1;

public class GlobalData {
    private static GlobalData instance;
    private String customerInfo;

    private GlobalData() {
        // Khởi tạo
    }

    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public void setRole(String info) {
        this.customerInfo = info;
    }

    public String getRole() {
        return this.customerInfo;
    }
}
