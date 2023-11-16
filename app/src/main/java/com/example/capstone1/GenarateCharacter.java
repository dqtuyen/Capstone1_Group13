package com.example.capstone1;

public class GenarateCharacter {
    public String getLastFiveCharacters(String input) {
        if (input != null && input.length() >= 5) {
            return input.substring(input.length() - 5);
        } else {
            // Xử lý khi chuỗi đầu vào có ít hơn 5 ký tự
            return input;
        }
    }
    public String convertDateTimeFormat(String inputDateTime) {
        String formattedDateTime = inputDateTime.replaceAll("[:\\s-]", ""); // Loại bỏ các ký tự không cần thiết

        return formattedDateTime;
    }

    public String getKey(String key) {
        String[] parts = key.split("_");

        // Lấy phần tử đầu tiên từ mảng kết quả
        String desiredElement = parts[0] + "_" + parts[1];

        return desiredElement;
    }

    public String getUID(String key) {
        String[] parts = key.split("_");

        String desiredElement = parts[2];

        return desiredElement;
    }
}
