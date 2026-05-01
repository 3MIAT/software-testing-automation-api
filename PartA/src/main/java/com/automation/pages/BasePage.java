package com.automation.pages;

import com.automation.config.ConfigReader;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By myAccountMenu = By.cssSelector("#top-links a[title='My Account']");
    private final By registerLink = By.linkText("Register");
    private final By loginLink = By.linkText("Login");
    private final By logoutLink = By.linkText("Logout");
    private final By currencyToggle = By.cssSelector("#form-currency button.dropdown-toggle");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait", 15)));
    }

    public void openMyAccountRegister() {
        openMyAccountMenu();
        click(registerLink);
    }

    public void openMyAccountLogin() {
        openMyAccountMenu();
        click(loginLink);
    }

    public void logout() {
        openRoute("route=account/logout");
    }

    public boolean isLogoutVisible() {
        openMyAccountMenu();
        return !driver.findElements(logoutLink).isEmpty();
    }

    public void changeCurrency(String currencyCode) {
        click(currencyToggle);
        click(By.cssSelector("button[name='" + currencyCode + "']"));
    }

    protected void openRoute(String routeAndParameters) {
        driver.get(ConfigReader.getRouteUrl(routeAndParameters));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String value) {
        WebElement element = waitVisible(locator);
        element.clear();
        element.sendKeys(value == null ? "" : value);
    }

    protected String text(By locator) {
        return waitVisible(locator).getText();
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> visibleElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void openMyAccountMenu() {
        click(myAccountMenu);
    }
}
