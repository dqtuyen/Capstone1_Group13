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
    public String trimmedCustom(String address_line) {
        String fullAddress = address_line;

        // Tách chuỗi thành mảng sử dụng dấu phẩy làm dấu phân cách
        String[] parts = fullAddress.split(",");

        // Kiểm tra xem có ít nhất một phần trước khi lấy phần đầu tiên
        if (parts.length > 0) {
            // Lấy chuỗi trước dấu phẩy đầu tiên
            return parts[0].trim() + ", " + parts[1].trim() + ", " + parts[2].trim();
        } else {
            // Chuỗi không có dấu phẩy
            System.out.println("Chuỗi không có dấu phẩy trong địa chỉ.");
            return "";
        }
    }
    public String getUID(String key) {
        String[] parts = key.split("_");

        String desiredElement = parts[2];

        return desiredElement;
    }
    public String removeBrackets(String input) {
        return input.replaceAll("[\\[\\]]", "");
    }
}
