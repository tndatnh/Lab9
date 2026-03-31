package framework.base;

import framework.config.ConfigReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        // ✅ SỬA: đọc từ ConfigReader thay vì hardcode 15
        int waitSeconds = ConfigReader.getInstance().getExplicitWait();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        PageFactory.initElements(driver, this);
    }

    /**
     * Chờ element có thể click được rồi mới click.
     * Tránh lỗi ElementNotInteractableException khi element còn đang load.
     */
    protected void waitAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /**
     * Xóa nội dung cũ rồi gõ text mới vào.
     * Tránh trường hợp text bị nối vào dữ liệu cũ còn trong field.
     */
    protected void waitAndType(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Lấy text hiển thị của element, đã bỏ khoảng trắng thừa.
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Kiểm tra element có hiển thị không.
     * Trả về false thay vì throw exception khi không tìm thấy.
     */
    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Cuộn trang đến element — xử lý trường hợp element nằm ngoài màn hình.
     */
    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Chờ trang load hoàn toàn xong — gọi sau khi chuyển trang.
     */
    protected void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Lấy giá trị của một attribute HTML (ví dụ: value, placeholder, class...).
     */
    protected String getAttribute(WebElement element, String attr) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getAttribute(attr);
    }
}