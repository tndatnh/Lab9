package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".cart_item .btn_secondary")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNameElements;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /** Đếm số sản phẩm trong giỏ. Trả về 0 nếu rỗng, không throw exception */
    public int getItemCount() {
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /** Xóa sản phẩm đầu tiên khỏi giỏ */
    public CartPage removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            waitAndClick(removeButtons.get(0));
        }
        return this;
    }

    /** Lấy danh sách tên các sản phẩm trong giỏ */
    public List<String> getItemNames() {
        return itemNameElements.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    /** Chuyển sang trang checkout */
    public CheckoutPage goToCheckout() {
        waitAndClick(checkoutButton);
        return new CheckoutPage(driver);
    }
}