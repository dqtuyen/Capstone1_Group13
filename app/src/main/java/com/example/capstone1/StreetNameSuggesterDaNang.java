package com.example.capstone1;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StreetNameSuggesterDaNang {
    private List<String> streets;

    public StreetNameSuggesterDaNang() {
        streets = new ArrayList<>();
        streets.add("Cao Thắng");
        streets.add("Lê Đình Lý");
        streets.add("Hùng Vương");
        streets.add("Lê Lợi");
        streets.add("3.2");
        streets.add("Nguyễn Chí Thanh");
        streets.add("Phan Châu Trinh");
        streets.add("Tiểu La");
        streets.add("2.9");
        streets.add("Quang Trung");
        streets.add("Đống Đa");
        streets.add("Nguyễn Du");
        streets.add("Lý Tự Trọng");
        streets.add("Hoàng Diệu");
        streets.add("Ông Ích Khiêm");
        streets.add("Nguyễn Hoàng");
        streets.add("Thái Phiên");
        streets.add("Lê Hồng Phong");
        streets.add("Hoàng Văn Thụ");
        streets.add("Yên Bái");
        streets.add("Trưng Nữ Vương");
        streets.add("Ngô Gia Tự");
        streets.add("Triệu Nữ Vương");
        streets.add("Bắc Đẩu");
        streets.add("Hải Hồ");
        streets.add("Pasteur");
        streets.add("Lê Đình Dương");
        streets.add("Nguyễn Văn Linh");
        streets.add("Lê Thanh Nghị");
        streets.add("Xô Viết Nghệ Tĩnh");
        streets.add("Trần Phú");
        streets.add("Bạch Đằng");
        streets.add("Nguyễn Tất Thành");
        streets.add("Thái Thị Bôi");
        streets.add("Lê Độ");
        streets.add("Trường Chinh");
        streets.add("Hải Phòng");
        streets.add("Điện Biên Phủ");
        streets.add("Hà Huy Tập");
        streets.add("Trần Cao Vân");
        streets.add("Nguyễn Tri Phương");
        streets.add("Hùng Vương");
        streets.add("Lê Đình Lý");
        streets.add("Duy Tân");
        streets.add("Tiểu La");
        streets.add("Nguyễn Phước Nguyên");
        streets.add("Xô Viết Nghệ Tĩnh");
        streets.add("2.9");
        streets.add("Nguyễn Lương Bằng");
        streets.add("Nguyễn tất Thành");
        streets.add("Bàu Tràm");
        streets.add("Âu Cơ");
        streets.add("Đoàn Phú Thứ");
        streets.add("Nam Cao");
        streets.add("Lạc Long Quân");
        streets.add("Nguyễn Sinh Sắc");
        streets.add("Kinh Dương Vương");
        streets.add("Nguyên Chánh");
        streets.add("Trần Đình Tri");
        streets.add("Hoàng Văn Thái");
        streets.add("Tố Hữu");
        streets.add("Tôn Đức Thắng");
        streets.add("Yết Kiêu");
        streets.add("Lý Tử Tấn");
        streets.add("Hoàng Sa");
        streets.add("Lê Đức Thọ");
        streets.add("Phan Bá Phiến");
        streets.add("Trần Nhân Tông");
        streets.add("Trương Định");
        streets.add("Chu Huy Mân");
        streets.add("Nại Hiên Đông");
        streets.add("Dương Lâm");
        streets.add("Lê Văn Duyệt");
        streets.add("Dương Vân Nga");
        streets.add("Lê Chân");
        streets.add("Lý Đạo Hành");
        streets.add("Ngô Quyền");
        streets.add("Lê Văn Thứ");
        streets.add("Võ Nguyên Giáp");
        streets.add("Hồ Nghinh");
        streets.add("Nguyễn Công Trứ");
        streets.add("Võ Văn Kiệt");
        streets.add("Lê Hữu Trác");
        streets.add("Nguyễn Văn Thoại");
        streets.add("Trần Hưng Đạo");
        streets.add("Lê Văn Duyệt");
        streets.add("Võ Nguyên Giáp");
        streets.add("Lê Quang Đạo");
        streets.add("Châu Thị Vĩnh tế");
        streets.add("Phan Tứ");
        streets.add("Trần Văn Dư");
        streets.add("Hồ Xuân Hương");
        streets.add("Bà Huyện Thanh Quan");
        streets.add("Võ Như Hưng");
        streets.add("Hoài Thanh");
        streets.add("An Dương Vương");
        streets.add("Phạm Hữu Kính");
        streets.add("Lê Văn Hưu");
        streets.add("Hồ Huân Nghiệp");
        streets.add("Nguyễn Lữ");
        streets.add("Đoàn Khuê");
        streets.add("Trịnh Lỗi");
        streets.add("Nghiêm Xuân Yêm");
        streets.add("Minh Mạng");
        streets.add("Nguyễn Xiến");
        streets.add("Trường Sa");
        streets.add("Nguyễn Duy Trinh");
        streets.add("Mai Đăng Chơn");
        streets.add("Võ Chí Công");
        streets.add("Phạm Đức Nam");
        streets.add("Lê Quát");
        streets.add("Trường Chinh");
        streets.add("Tôn Đản");
        streets.add("Lê Trọng Tấn");
        streets.add("Lê Đại Hành");
        streets.add("Xô Viết Nghệ Tĩnh");
        streets.add("Thăng Long");
        streets.add("Phạm Tứ");
        streets.add("Phan Khôi");
        streets.add("Lê Quang Định");
        streets.add("Trần Nam Trung");
        streets.add("Võ Chí Công");
        streets.add("Hồ Sĩ Dương");
        streets.add("Quốc lộ 14B");
    }

    public List<String> getAllStreetNames() {
        // Trả về tất cả tên đường trong danh sách
        return streets;
    }

    public String suggestStreetName() {
        // Gợi ý một tên đường ngẫu nhiên từ danh sách
        Random random = new Random();
        int randomIndex = random.nextInt(streets.size());
        return streets.get(randomIndex);
    }
    public List<String> suggestStreetByName(String prefix) {
        // Loại bỏ ký tự số và dấu space sau ký tự số từ chuỗi đầu vào
        String removeDigits = removeSlash(prefix);

        String cleanedPrefix = removeDigitsAndFollowingSpace(removeDigits);

        // Chuẩn hóa chuỗi (loại bỏ dấu, ký tự '/' và chuyển về chữ thường)
        String normalizedInput = normalizeString(cleanedPrefix);

        List<String> suggestedStreets = new ArrayList<>();
        for (String street : streets) {
            // Chuẩn hóa tên đường và kiểm tra xem tiền tố có trong đường hay không
            if (normalizeString(street).contains(normalizedInput)) {
                suggestedStreets.add(street);
            }
        }
        return suggestedStreets;
    }

    private String removeDigitsAndFollowingSpace(String input) {
        // Loại bỏ ký tự số và dấu space sau ký tự số từ chuỗi
        return input.replaceAll("\\d\\s*", "");
    }
    private String removeSlash(String input) {
        // Loại bỏ ký tự /
        return input.replaceAll("/", "");
    }
    private String normalizeString(String input) {
        // Loại bỏ dấu và ký tự '/' và chuyển về chữ thường
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        return normalized.toLowerCase();
    }
}