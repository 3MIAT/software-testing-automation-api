package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By emailInput = By.id("input-email");
    private final By passwordInput = By.id("input-password");
    private final By loginButton = By.cssSelector("input[type='submit'][value='Login']");
    private final By warningAlert = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getWarningMessage() {
        return text(warningAlert);
    }

    public boolean isWarningDisplayed() {
        return !driver.findElements(warningAlert).isEmpty();
    }
}
