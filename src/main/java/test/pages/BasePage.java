package test.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import test.config.Config;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By myAccountMenu = By.cssSelector("#top-links a[title='My Account']");
    private final By myAccountRegister = By.linkText("Register");
    private final By myAccountLogin = By.linkText("Login");
    private final By myAccountLogout = By.linkText("Logout");
    private final By currencyToggle = By.cssSelector("#form-currency button.dropdown-toggle");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int explicitWaitSeconds = Config.getInt("explicitWait", 10);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));
    }

    public void openMyAccountRegister() {
        openMyAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(myAccountRegister)).click();
    }

    public void openMyAccountLogin() {
        openMyAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(myAccountLogin)).click();
    }

    public void logout() {
        openMyAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(myAccountLogout)).click();
    }

    public boolean isLogoutVisible() {
        openMyAccountMenu();
        return !driver.findElements(myAccountLogout).isEmpty();
    }

    public void changeCurrency(String currencyButtonName) {
        wait.until(ExpectedConditions.elementToBeClickable(currencyToggle)).click();
        By currencyOption = By.cssSelector("button[name='" + currencyButtonName + "']");
        wait.until(ExpectedConditions.elementToBeClickable(currencyOption)).click();
    }

    private void openMyAccountMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountMenu)).click();
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
