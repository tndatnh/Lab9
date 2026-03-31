package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Đăng nhập thành công với tài khoản hợp lệ")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());

        // Fluent Interface: login() trả về InventoryPage ngay
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        // Assert nằm trong TEST CLASS, không nằm trong Page Object
        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load!");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                "URL không chứa 'inventory'");
    }

    @Test(description = "Đăng nhập thất bại - sai mật khẩu")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(getDriver());

        // loginExpectingFailure() trả về LoginPage (không đi đâu cả)
        LoginPage result = loginPage.loginExpectingFailure("standard_user", "wrongpass");

        Assert.assertTrue(result.isErrorDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertTrue(result.getErrorMessage().contains("do not match"),
                "Nội dung lỗi không đúng: " + result.getErrorMessage());
    }

    @Test(description = "Đăng nhập thất bại - tài khoản bị khoá")
    public void testLoginLockedUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("locked_out_user", "secret_sauce");

        Assert.assertTrue(result.isErrorDisplayed());
        Assert.assertTrue(result.getErrorMessage().contains("locked out"));
    }

    @Test(description = "Đăng nhập thất bại - để trống username")
    public void testLoginEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        LoginPage result = loginPage.loginExpectingFailure("", "secret_sauce");

        Assert.assertTrue(result.isErrorDisplayed());
        Assert.assertTrue(result.getErrorMessage().contains("Username is required"));
    }
}