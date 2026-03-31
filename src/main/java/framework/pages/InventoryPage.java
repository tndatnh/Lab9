package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    // List<WebElement> = lấy TẤT CẢ nút "Add to cart" trên trang
    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    /** Kiểm tra trang đã load xong chưa — dùng để assert trong test */
    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    /** Thêm sản phẩm đầu tiên trong danh sách vào giỏ */
    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons.get(0)); // .get(0) = phần tử đầu tiên
        return this; // Trả về this để có thể gọi tiếp: .addFirstItemToCart().goToCart()
    }

    /**
     * Thêm sản phẩm theo tên cụ thể.
     * Duyệt qua danh sách tên, tìm đúng tên rồi click nút Add to Cart tương ứng.
     */
    public InventoryPage addItemByName(String name) {
        for (int i = 0; i < itemNames.size(); i++) {
            if (itemNames.get(i).getText().equals(name)) {
                waitAndClick(addToCartButtons.get(i));
                return this;
            }
        }
        throw new RuntimeException("Không tìm thấy sản phẩm: " + name);
    }

    /** Lấy số lượng item hiện trong badge giỏ hàng (góc trên phải) */
    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText().trim());
        } catch (Exception e) {
            return 0; // Badge không hiện = giỏ hàng rỗng
        }
    }

    /** Chuyển sang trang giỏ hàng */
    public CartPage goToCart() {
        driver.findElement(By.className("shopping_cart_link")).click();
        return new CartPage(driver);
    }
}