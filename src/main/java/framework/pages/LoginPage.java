package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // @FindBy thay cho driver.findElement() — locator nằm ở đây, không nằm trong test
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver); // Gọi BasePage constructor → khởi tạo wait + PageFactory
    }

    /**
     * Đăng nhập khi mong đợi thành công.
     * Trả về InventoryPage vì sau login thành công sẽ vào trang đó.
     */
    public InventoryPage login(String username, String password) {
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        return new InventoryPage(driver);
    }

    /**
     * Đăng nhập khi biết trước sẽ thất bại (sai pass, account bị khoá...).
     * Trả về LoginPage vì trang không chuyển đi đâu cả.
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        waitAndType(usernameField, username);
        waitAndType(passwordField, password);
        waitAndClick(loginButton);
        return this; // "this" = chính LoginPage hiện tại
    }

    /** Lấy nội dung thông báo lỗi hiển thị trên trang */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /** Kiểm tra thông báo lỗi có đang hiển thị không */
    public boolean isErrorDisplayed() {
        return isElementVisible(By.cssSelector("[data-test='error']"));
    }
}