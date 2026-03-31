package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();
    private static ConfigReader instance; // Singleton: chỉ có 1 instance

    // Constructor private: không ai new ConfigReader() từ bên ngoài được
    private ConfigReader() {
        // Đọc biến env từ System, mặc định là "dev" nếu không truyền vào
        String env = System.getProperty("env", "dev");
        String filePath = "src/test/resources/config-" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
            System.out.println("[ConfigReader] ✅ Đang dùng môi trường: " + env);
            System.out.println("[ConfigReader] Base URL: " + props.getProperty("base.url"));
            System.out.println("[ConfigReader] Explicit Wait: " + props.getProperty("explicit.wait") + "s");
        } catch (IOException e) {
            throw new RuntimeException("❌ Không tìm thấy file config: " + filePath, e);
        }
    }

    // Gọi method này để lấy instance — tạo mới nếu chưa có
    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    // Reset instance — dùng khi test cần đổi env giữa các lần chạy
    public static void reset() {
        instance = null;
    }

    public String getBaseUrl() {
        return props.getProperty("base.url");
    }

    public String getBrowser() {
        return props.getProperty("browser", "chrome");
    }

    public int getExplicitWait() {
        return Integer.parseInt(props.getProperty("explicit.wait", "15"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(props.getProperty("implicit.wait", "5"));
    }

    public int getRetryCount() {
        return Integer.parseInt(props.getProperty("retry.count", "1"));
    }

    public String getScreenshotPath() {
        return props.getProperty("screenshot.path", "target/screenshots/");
    }
}