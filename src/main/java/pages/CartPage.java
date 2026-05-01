package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private By cartTotalButton = By.id("cart-total");
    private By viewCartLink = By.xpath("//strong[normalize-space()='View Cart']");
    private By checkoutLink = By.xpath("//strong[normalize-space()='Checkout']");
    
    // Elements on the main cart page
    private By cartItemNames = By.cssSelector(".table-bordered tbody tr td.text-left a");
    private By cartTotalValues = By.cssSelector(".table-bordered tbody tr td:nth-child(6)");
    private By finalTotalRows = By.cssSelector(".col-sm-4.col-sm-offset-8 table.table-bordered tr");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void openCartDropdown() {
        click(cartTotalButton);
    }

    public void clickOnViewCart() {
        openCartDropdown();
        click(viewCartLink);
    }

    public void clickOnCheckout() {
        openCartDropdown();
        click(checkoutLink);
    }

    public boolean isItemInCart(String itemName) {
        try {
            List<WebElement> items = getElements(cartItemNames);
            for (WebElement item : items) {
                if (item.getText().contains(itemName)) {
                    return true;
                }
            }
        } catch (Exception e) {}
        return false;
    }

    public String getCartTotal() {
        List<WebElement> rows = getElements(finalTotalRows);
        for (WebElement row : rows) {
            if (row.getText().contains("Total:")) {
                return row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            }
        }
        return null;
    }
}
