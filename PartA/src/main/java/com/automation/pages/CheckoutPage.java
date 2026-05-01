package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class CheckoutPage extends BasePage {
    private final By guestCheckoutRadio = By.cssSelector("input[name='account'][value='guest']");
    private final By continueAccountButton = By.id("button-account");
    private final By newBillingAddressRadio = By.cssSelector("input[name='payment_address'][value='new']");
    private final By firstNameInput = By.id("input-payment-firstname");
    private final By lastNameInput = By.id("input-payment-lastname");
    private final By addressInput = By.id("input-payment-address-1");
    private final By cityInput = By.id("input-payment-city");
    private final By postCodeInput = By.id("input-payment-postcode");
    private final By countrySelect = By.id("input-payment-country");
    private final By regionSelect = By.id("input-payment-zone");
    private final By continueBillingButton = By.id("button-payment-address");
    private final By continueShippingButton = By.id("button-shipping-address");
    private final By deliveryCommentBox = By.name("comment");
    private final By continueDeliveryButton = By.id("button-shipping-method");
    private final By termsCheckbox = By.name("agree");
    private final By continuePaymentButton = By.id("button-payment-method");
    private final By confirmOrderButton = By.id("button-confirm");
    private final By successMessage = By.cssSelector("#content h1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void continueBillingWithExistingAddress() {
        click(continueBillingButton);
    }

    public void continueBillingWithNewAddress(String firstName, String lastName, String address, String city, String postCode) {
        openBillingAddressForm();
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(addressInput, address);
        type(cityInput, city);
        type(postCodeInput, postCode);
        new Select(waitVisible(countrySelect)).selectByValue("222");
        wait.until(driver -> new Select(waitVisible(regionSelect)).getOptions().size() > 1);
        new Select(waitVisible(regionSelect)).selectByValue("3563");
        click(continueBillingButton);
    }

    private void openBillingAddressForm() {
        if (!driver.findElements(guestCheckoutRadio).isEmpty()) {
            click(guestCheckoutRadio);
            click(continueAccountButton);
        }
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(newBillingAddressRadio),
                ExpectedConditions.presenceOfElementLocated(firstNameInput)
        ));
        if (!driver.findElements(newBillingAddressRadio).isEmpty()) {
            if (!driver.findElement(newBillingAddressRadio).isSelected()) {
                click(newBillingAddressRadio);
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        }
    }

    public void continueShippingAddress() {
        click(continueShippingButton);
    }

    public void enterDeliveryComment(String comment) {
        type(deliveryCommentBox, comment);
    }

    public void continueDeliveryMethod() {
        click(continueDeliveryButton);
    }

    public void acceptTermsAndConditions() {
        if (!waitVisible(termsCheckbox).isSelected()) {
            click(termsCheckbox);
        }
    }

    public void continuePaymentMethod() {
        click(continuePaymentButton);
    }

    public void confirmOrder() {
        click(confirmOrderButton);
    }

    public String getSuccessMessage() {
        return text(successMessage);
    }
}
