package framework.base;
import framework.config.ConfigReader;
import framework.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
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

    @Parameters({ "browser", "env" })
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("dev") String env) {

        // Set env trước khi load config
        System.setProperty("env", env);
        ConfigReader.reset();

        ConfigReader config = ConfigReader.getInstance();

        // ✅ QUAN TRỌNG: dùng DriverFactory + truyền browser
        WebDriver driver = DriverFactory.createDriver(browser);

        driver.manage().window().maximize();

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

            System.out.println("[Screenshot] Saved: " + screenshotPath + fileName);

        } catch (Exception e) {
            System.out.println("[Screenshot] Error: " + e.getMessage());
        }
    }
}