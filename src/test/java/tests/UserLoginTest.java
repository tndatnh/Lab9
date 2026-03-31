package tests;
import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import framework.utils.UserData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;

public class UserLoginTest extends BaseTest {
    /**
     * @DataProvider: TestNG sẽ gọi method này để lấy dữ liệu
     * Mỗi Object[] trong mảng = 1 lần chạy test
     */
    @DataProvider(name = "jsonUsers")
    public Object[][] getUsersFromJson() throws IOException {
        List<UserData> users = JsonReader.readUsers(
                "src/test/resources/testdata/users.json"
        );

        // Chuyển List<UserData> sang Object[][] cho TestNG
        return users.stream()
                .map(u -> new Object[]{u.username, u.password, u.expectSuccess, u.description})
                .toArray(Object[][]::new);
    }

    /**
     * Test này sẽ chạy 5 lần — mỗi lần với 1 bộ dữ liệu từ JSON.
     * Tham số tự động được inject từ @DataProvider.
     */
    @Test(dataProvider = "jsonUsers", description = "Test đăng nhập từ JSON")
    public void testLoginFromJson(String username, String password,
                                  boolean expectSuccess, String description) {
        System.out.println("=== Đang test: " + description + " ===");

        LoginPage loginPage = new LoginPage(getDriver());

        if (expectSuccess) {
            InventoryPage inventoryPage = loginPage.login(username, password);
            Assert.assertTrue(inventoryPage.isLoaded(),
                    "[FAIL] " + description + " - Inventory page không load");
        } else {
            LoginPage result = loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(result.isErrorDisplayed(),
                    "[FAIL] " + description + " - Không có thông báo lỗi");
        }
    }
}