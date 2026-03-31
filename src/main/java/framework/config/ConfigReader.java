package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private Properties properties;
    private static final String CONFIG_PATH = "src/main/resources/config.properties";

    private ConfigReader() {
        properties = new Properties();
        loadConfig();
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Không tìm thấy file config: " + CONFIG_PATH);
        }
    }

    public String getProperty(String key) {
        // ✅ ƯU TIÊN: Đọc từ biến môi trường (khi chạy trên CI/CD)
        String envValue = System.getenv(key.toUpperCase().replace(".", "_"));
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        
        // ✅ FALLBACK: Đọc từ file config (khi chạy local)
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return getProperty("app.base.url");
    }

    public String getUsername() {
        // ✅ Đọc username: ưu tiên env var, fallback về config
        String username = System.getenv("APP_USERNAME");
        if (username != null && !username.isBlank()) {
            return username;
        }
        return getProperty("app.username");
    }

    public String getPassword() {
        // ✅ Đọc password: ưu tiên env var, fallback về config
        String password = System.getenv("APP_PASSWORD");
        if (password != null && !password.isBlank()) {
            return password;
        }
        return getProperty("app.password");
    }

    public long getImplicitWait() {
        return Long.parseLong(getProperty("app.implicit.wait"));
    }

    public String getScreenshotPath() {
        return getProperty("app.screenshot.path");
    }
}
