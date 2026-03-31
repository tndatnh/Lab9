package framework.utils;

import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class TestDataFactory {

    // Dùng Locale("en") vì SauceDemo là web tiếng Anh
    private static final Faker faker = new Faker(new Locale("en"));

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    /** Sinh mã bưu chính 5 chữ số ngẫu nhiên */
    public static String randomPostalCode() {
        return faker.number().digits(5);
    }

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    /**
     * Trả về một Map chứa đầy đủ thông tin checkout ngẫu nhiên.
     * Gọi method này mỗi lần để có dữ liệu khác nhau.
     */
    public static Map<String, String> randomCheckoutData() {
        Map<String, String> data = new HashMap<>();
        data.put("firstName", randomFirstName());
        data.put("lastName", randomLastName());
        data.put("postalCode", randomPostalCode());
        return data;
    }
}