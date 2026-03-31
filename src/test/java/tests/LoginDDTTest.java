package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginDDTTest extends BaseTest {

    // Đường dẫn tới file Excel — dùng chung cho tất cả DataProvider
    private static final String EXCEL_PATH =
            "src/test/resources/testdata/login_data.xlsx";

    // ============================================================
    // DATA PROVIDERS — mỗi provider đọc 1 sheet
    // ============================================================

    @DataProvider(name = "smokeData")
    public Object[][] getSmokeData() {
        return ExcelReader.getData(EXCEL_PATH, "SmokeCases");
    }

    @DataProvider(name = "negativeData")
    public Object[][] getNegativeData() {
        return ExcelReader.getData(EXCEL_PATH, "NegativeCases");
    }

    @DataProvider(name = "boundaryData")
    public Object[][] getBoundaryData() {
        return ExcelReader.getData(EXCEL_PATH, "BoundaryCases");
    }

    // ============================================================
    // SMOKE TESTS — đăng nhập thành công
    // Cột Excel: username | password | expected_url | description
    // ============================================================

    @Test(
            dataProvider = "smokeData",
            groups = {"smoke", "regression"},
            description = "Smoke: đăng nhập thành công"
    )
    public void testLoginSuccess(String username, String password,
                                 String expectedUrl, String description) {
        System.out.println("=== [SMOKE] " + description + " ===");

        InventoryPage inventoryPage = new LoginPage(getDriver())
                .login(username, password);

        Assert.assertTrue(
                inventoryPage.isLoaded(),
                "[FAIL] " + description + " — Inventory page không load"
        );
        Assert.assertTrue(
                getDriver().getCurrentUrl().contains(expectedUrl),
                "[FAIL] URL không chứa '" + expectedUrl + "'"
        );
    }

    // ============================================================
    // NEGATIVE TESTS — đăng nhập thất bại
    // Cột Excel: username | password | expected_error | description
    // ============================================================

    @Test(
            dataProvider = "negativeData",
            groups = {"regression"},
            description = "Negative: đăng nhập thất bại"
    )
    public void testLoginFailure(String username, String password,
                                 String expectedError, String description) {
        System.out.println("=== [NEGATIVE] " + description + " ===");

        LoginPage result = new LoginPage(getDriver())
                .loginExpectingFailure(username, password);

        Assert.assertTrue(
                result.isErrorDisplayed(),
                "[FAIL] " + description + " — Không có thông báo lỗi"
        );
        Assert.assertTrue(
                result.getErrorMessage().contains(expectedError),
                "[FAIL] " + description
                        + "\n  Mong đợi: " + expectedError
                        + "\n  Thực tế : " + result.getErrorMessage()
        );
    }

    // ============================================================
    // BOUNDARY TESTS — dữ liệu biên
    // Cột Excel: username | password | expected_error | description
    // ============================================================

    @Test(
            dataProvider = "boundaryData",
            groups = {"regression"},
            description = "Boundary: dữ liệu biên và injection"
    )
    public void testLoginBoundary(String username, String password,
                                  String expectedError, String description) {
        System.out.println("=== [BOUNDARY] " + description + " ===");

        LoginPage result = new LoginPage(getDriver())
                .loginExpectingFailure(username, password);

        Assert.assertTrue(
                result.isErrorDisplayed(),
                "[FAIL] " + description + " — Không có thông báo lỗi"
        );
        Assert.assertTrue(
                result.getErrorMessage().contains(expectedError),
                "[FAIL] " + description
                        + "\n  Mong đợi: " + expectedError
                        + "\n  Thực tế : " + result.getErrorMessage()
        );
    }
}