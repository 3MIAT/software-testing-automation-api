package test.pages;

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
        waitVisible(emailInput).clear();
        waitVisible(emailInput).sendKeys(email);
        waitVisible(passwordInput).clear();
        waitVisible(passwordInput).sendKeys(password);
        waitVisible(loginButton).click();
    }

    public String getWarningMessage() {
        return waitVisible(warningAlert).getText();
    }
}
