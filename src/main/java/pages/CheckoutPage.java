package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    // Billing Details
    private By billingAddressDropdown = By.name("address_id");
    private By continueBillingButton = By.id("button-payment-address");

    // Shipping Details
    private By continueShippingButton = By.id("button-shipping-address");

    // Delivery Method
    private By continueDeliveryButton = By.id("button-shipping-method");
    private By deliveryCommentBox = By.name("comment");

    // Payment Method
    private By termsCheckbox = By.name("agree");
    private By continuePaymentButton = By.id("button-payment-method");

    // Confirm Order
    private By confirmOrderButton = By.id("button-confirm");
    private By successMessage = By.cssSelector("#content h1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void selectExistingBillingAddress() {
        // Assume default selection is the existing address, just wait for the button
        click(continueBillingButton);
    }

    public void clickContinueBilling() {
        click(continueBillingButton);
    }

    public void clickContinueShipping() {
        click(continueShippingButton);
    }

    public void enterDeliveryComment(String comment) {
        enterText(deliveryCommentBox, comment);
    }

    public void clickContinueDelivery() {
        click(continueDeliveryButton);
    }

    public void acceptTermsAndConditions() {
        if (!driver.findElement(termsCheckbox).isSelected()) {
            click(termsCheckbox);
        }
    }

    public void clickContinuePayment() {
        click(continuePaymentButton);
    }

    public void confirmOrder() {
        click(confirmOrderButton);
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }
}
