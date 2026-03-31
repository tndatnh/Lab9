package tests;
import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    
    @Test(description = "Đăng nhập thành công với tài khoản hợp lệ")
    public void testLoginSuccess() {
        // ✅ Đọc credential từ ConfigReader (ưu tiên env var → fallback config file)
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();
        
        LoginPage loginPage = new LoginPage(getDriver());

        // Fluent Interface: login() trả về InventoryPage ngay
        InventoryPage inventoryPage = loginPage.login(username, password);

        // Assert nằm trong TEST CLASS, không nằm trong Page Object
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load!");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                "URL không chứa 'inventory'");
    }

    @Test(description = "Đăng nhập thất bại - sai mật khẩu")
    public void testLoginWrongPassword() {
        String username = ConfigReader.getInstance().getUsername();
        String wrongPassword = "wrongpass"; // ✅ Password sai cố ý cho test negative
        
        LoginPage loginPage = new LoginPage(getDriver());

        // loginExpectingFailure() trả về LoginPage (không đi đâu cả)
        LoginPage result = loginPage.loginExpectingFailure(username, wrongPassword);

        Assert.assertTrue(result.isErrorDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertTrue(result.getErrorMessage().contains("do not match"),
                "Nội dung lỗi không đúng: " + result.getErrorMessage());
    }

    @Test(description = "Đăng nhập thất bại - tài khoản bị khoá")
    public void testLoginLockedUser() {
        // ✅ Username locked_out_user là test data cố định, không phải credential thật
        String lockedUser = "locked_out_user";
        String password = ConfigReader.getInstance().getPassword();
        
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure(lockedUser, password);

        Assert.assertTrue(result.isErrorDisplayed());
        Assert.assertTrue(result.getErrorMessage().contains("locked out"));
    }

    @Test(description = "Đăng nhập thất bại - để trống username")
    public void testLoginEmptyUsername() {
        String emptyUsername = "";
        String password = ConfigReader.getInstance().getPassword();
        
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure(emptyUsername, password);

        Assert.assertTrue(result.isErrorDisplayed());
        Assert.assertTrue(result.getErrorMessage().contains("Username is required"));
    }
}
