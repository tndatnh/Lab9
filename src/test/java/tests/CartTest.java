package tests;
import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {
    
    @Test(description = "Thêm 1 sản phẩm vào giỏ - kiểm tra badge số lượng")
    public void testAddItemToCart() {
        // ✅ Đọc credential từ ConfigReader
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();
        
        // Fluent Interface: chuỗi method liên tiếp
        InventoryPage inventoryPage = new LoginPage(getDriver())
                .login(username, password);

        inventoryPage.addFirstItemToCart();

        Assert.assertEquals(inventoryPage.getCartItemCount(), 1,
                "Badge giỏ hàng phải hiện số 1");
    }

    @Test(description = "Kiểm tra sản phẩm xuất hiện trong CartPage")
    public void testItemAppearsInCart() {
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();
        
        CartPage cartPage = new LoginPage(getDriver())
                .login(username, password)
                .addFirstItemToCart()
                .goToCart(); // Fluent Interface hoạt động!

        Assert.assertEquals(cartPage.getItemCount(), 1,
                "Giỏ hàng phải có 1 sản phẩm");
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ - giỏ trở thành rỗng")
    public void testRemoveItemFromCart() {
        String username = ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();
        
        CartPage cartPage = new LoginPage(getDriver())
                .login(username, password)
                .addFirstItemToCart()
                .goToCart()
                .removeFirstItem();

        Assert.assertEquals(cartPage.getItemCount(), 0,
                "Giỏ hàng phải rỗng sau khi xóa");
    }
}
