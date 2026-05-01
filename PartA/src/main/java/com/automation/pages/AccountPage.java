package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage extends BasePage {
    private final By firstNameInput = By.id("input-firstname");
    private final By lastNameInput = By.id("input-lastname");
    private final By emailInput = By.id("input-email");
    private final By telephoneInput = By.id("input-telephone");
    private final By passwordInput = By.id("input-password");
    private final By confirmPasswordInput = By.id("input-confirm");
    private final By agreeCheckbox = By.name("agree");
    private final By continueButton = By.cssSelector("input[type='submit'][value='Continue']");
    private final By successHeading = By.cssSelector("#content h1");
    private final By accountHeader = By.cssSelector("#content h2");

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        type(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {
        type(lastNameInput, lastName);
    }

    public void enterEmail(String email) {
        type(emailInput, email);
    }

    public void enterTelephone(String telephone) {
        type(telephoneInput, telephone);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        type(confirmPasswordInput, confirmPassword);
    }

    public void setAgree(boolean value) {
        boolean selected = waitVisible(agreeCheckbox).isSelected();
        if (selected != value) {
            click(agreeCheckbox);
        }
    }

    public void submitRegistration() {
        click(continueButton);
    }

    public String getSuccessHeading() {
        return text(successHeading);
    }

    public String getAccountHeader() {
        return text(accountHeader);
    }

    public String getFieldErrorByInputId(String inputId) {
        return text(By.cssSelector("#" + inputId + " + .text-danger"));
    }

    public String getPasswordErrorMessage() {
        return getFieldErrorByInputId("input-password");
    }
}
