package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    public static WebDriver createDriver() {
        boolean isCI = System.getenv("CI") != null;

        ChromeOptions options = new ChromeOptions();

        if (isCI) {
            System.out.println("Running on CI → HEADLESS mode");

            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            System.out.println("Running LOCAL → normal mode");
            options.addArguments("--start-maximized");
        }

        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }
}