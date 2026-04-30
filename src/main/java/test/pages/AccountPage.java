package test.pages;

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
        waitVisible(firstNameInput).clear();
        waitVisible(firstNameInput).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        waitVisible(lastNameInput).clear();
        waitVisible(lastNameInput).sendKeys(lastName);
    }

    public void enterEmail(String email) {
        waitVisible(emailInput).clear();
        waitVisible(emailInput).sendKeys(email);
    }

    public void enterTelephone(String telephone) {
        waitVisible(telephoneInput).clear();
        waitVisible(telephoneInput).sendKeys(telephone);
    }

    public void enterPassword(String password) {
        waitVisible(passwordInput).clear();
        waitVisible(passwordInput).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        waitVisible(confirmPasswordInput).clear();
        waitVisible(confirmPasswordInput).sendKeys(confirmPassword);
    }

    public void setAgree(boolean value) {
        if (value) {
            if (!waitVisible(agreeCheckbox).isSelected()) {
                waitVisible(agreeCheckbox).click();
            }
        } else {
            if (waitVisible(agreeCheckbox).isSelected()) {
                waitVisible(agreeCheckbox).click();
            }
        }
    }

    public void submitRegistration() {
        waitVisible(continueButton).click();
    }

    public String getSuccessHeading() {
        return waitVisible(successHeading).getText();
    }

    public String getAccountHeader() {
        return waitVisible(accountHeader).getText();
    }

    public String getFieldErrorByInputId(String inputId) {
        By errorLocator = By.cssSelector("#" + inputId + " + .text-danger");
        return waitVisible(errorLocator).getText();
    }

    public String getPasswordErrorMessage() {
        return getFieldErrorByInputId("input-password");
    }
}
