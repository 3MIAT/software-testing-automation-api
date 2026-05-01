package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {
    private final By deliveryDateInput = By.id("input-option225");
    private final By addToCartButton = By.id("button-cart");
    private final By successAlert = By.cssSelector(".alert.alert-success");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void setDeliveryDate(String date) {
        type(deliveryDateInput, date);
    }

    public void addToCart() {
        click(addToCartButton);
    }

    public String getSuccessAlertText() {
        return text(successAlert);
    }
}
