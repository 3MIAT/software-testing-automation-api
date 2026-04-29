package tests;

import base.BaseTest;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;

public class CartAndCheckoutTests extends BaseTest {

    @Test(description = "TC-10: Add items to shopping cart & compare")
    public void testAddItemsToCart_TC10() {
        // Assume Login logic here (using a dummy login for test flow)
        login();

        // Navigate to Tablets
        driver.get(ConfigReader.getProperty("url") + "&path=57");
        
        // Add "Samsung Galaxy Tab 10.1" to the cart
        driver.findElement(By.xpath("//h4/a[contains(text(), 'Samsung Galaxy Tab 10.1')]/ancestor::div[contains(@class, 'product-thumb')]//button[contains(@onclick, 'cart.add')]")).click();

        // Navigate to Laptops
        driver.get(ConfigReader.getProperty("url") + "&path=18");
        
        // Add "HP LP3065" laptop
        driver.findElement(By.xpath("//h4/a[contains(text(), 'HP LP3065')]/ancestor::div[contains(@class, 'product-thumb')]//button[contains(@onclick, 'cart.add')]")).click();
        
        // Handling the date selection for HP LP3065
        WebElement dateInput = driver.findElement(By.id("input-option225"));
        dateInput.clear();
        dateInput.sendKeys("2024-12-31");
        driver.findElement(By.id("button-cart")).click();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickOnViewCart();

        // Assert items exist in cart
        Assert.assertTrue(cartPage.isItemInCart("Samsung Galaxy Tab 10.1"), "Tablet should be in the cart");
        Assert.assertTrue(cartPage.isItemInCart("HP LP3065"), "Laptop should be in the cart");

        // Assert Total price
        String total = cartPage.getCartTotal();
        Assert.assertNotNull(total, "Total price should be displayed in the cart");
    }

    @Test(description = "TC-11: Normal Checkout process and confirm order")
    public void testCheckoutProcess_TC11() {
        // Login
        login();

        // Navigate to MP3 Players -> Show all MP3 Players
        driver.get(ConfigReader.getProperty("url") + "&path=34");
        
        // Add "ipod shuffle" to the cart
        driver.findElement(By.xpath("//h4/a[contains(text(), 'iPod Shuffle')]/ancestor::div[contains(@class, 'product-thumb')]//button[contains(@onclick, 'cart.add')]")).click();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickOnViewCart();

        cartPage.clickOnCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // Billing Details
        checkoutPage.clickContinueBilling();
        
        // Shipping Details
        checkoutPage.clickContinueShipping();
        
        // Delivery Method
        checkoutPage.enterDeliveryComment("Please leave at the door.");
        checkoutPage.clickContinueDelivery();
        
        // Payment Method
        checkoutPage.acceptTermsAndConditions();
        checkoutPage.clickContinuePayment();

        // Confirm Order
        checkoutPage.confirmOrder();

        // Assert Success Message
        String successMessage = checkoutPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Your order has been placed!"), "Order should be confirmed successfully.");
    }

    private void login() {
        driver.get(ConfigReader.getProperty("url"));
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        
        WebElement myAccount = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='My Account']")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", myAccount);
        
        WebElement loginLink = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(By.linkText("Login")));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", loginLink);
        
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(By.id("input-email"))).sendKeys(ConfigReader.getProperty("email"));
        driver.findElement(By.id("input-password")).sendKeys(ConfigReader.getProperty("password"));
        driver.findElement(By.cssSelector("input[value='Login']")).click();
    }
}
