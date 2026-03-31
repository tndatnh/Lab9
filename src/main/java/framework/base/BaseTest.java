package framework.base;

import framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import framework.DriverFactory;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public abstract class BaseTest {

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    /**
     * @Parameters: nhận giá trị từ testng.xml
     * @Optional: nếu testng.xml không truyền thì dùng giá trị mặc định
     */
    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("dev") String env) {

        // Đặt env vào System property TRƯỚC KHI khởi tạo ConfigReader
        System.setProperty("env", env);
        ConfigReader.reset(); // Reset để đọc lại config đúng môi trường

        ConfigReader config = ConfigReader.getInstance();

        WebDriverManager.chromedriver().setup();
        WebDriver driver = DriverFactory.createDriver();
        driver.manage().window().maximize();

        // Dùng giá trị từ config thay vì hardcode
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));

        driver.get(config.getBaseUrl());
        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot(result.getName());
        }
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove();
        }
    }

    private void takeScreenshot(String testName) {
        try {
            String screenshotPath = ConfigReader.getInstance().getScreenshotPath();
            File dir = new File(screenshotPath);
            if (!dir.exists()) dir.mkdirs();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";

            File screenshot = ((TakesScreenshot) getDriver())
                    .getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(),
                    new File(screenshotPath + fileName).toPath());

            System.out.println("[Screenshot] ✅ Đã lưu: " + screenshotPath + fileName);
        } catch (Exception e) {
            System.out.println("[Screenshot] ❌ Lỗi: " + e.getMessage());
        }
    }
}