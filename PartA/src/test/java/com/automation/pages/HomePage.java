package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────
    private final By myAccountMenu    = By.cssSelector("a.dropdown-toggle[title='My Account']");
    private final By registerLink     = By.cssSelector("ul.dropdown-menu a[href*='register']");
    private final By loginLink        = By.cssSelector("ul.dropdown-menu a[href*='login']");
    private final By searchInput      = By.cssSelector("input[name='search']");
    private final By searchButton     = By.cssSelector("button.btn-default[type='submit']");
    private final By currencyDropdown = By.cssSelector("button[data-toggle='dropdown'].btn-link");
    private final By cartButton       = By.cssSelector("button#cart-total");
    private final By navbar           = By.cssSelector("nav#menu");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigation ────────────────────────────────────────────

    public void goToRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    public void goToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccountMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    // ── Search ────────────────────────────────────────────────

    public void searchFor(String keyword) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(keyword);
        driver.findElement(searchButton).click();
    }

    // ── Currency ──────────────────────────────────────────────

    public void selectCurrency(String currencySymbol) {
        // currencySymbol: "€ Euro", "£ Pound Sterling", "$ US Dollar"
        wait.until(ExpectedConditions.elementToBeClickable(currencyDropdown)).click();
        By currencyOption = By.xpath(
                "//ul[contains(@class,'dropdown-menu')]//button[contains(text(),'" + currencySymbol + "')]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(currencyOption)).click();
    }

    public String getCartButtonText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartButton)).getText();
    }

    // ── Navbar ────────────────────────────────────────────────

    public boolean isNavbarDisplayed() {
        return driver.findElement(navbar).isDisplayed();
    }

    public void clickNavbarCategory(String categoryName) {
        By categoryLink = By.xpath(
                "//nav[@id='menu']//a[normalize-space()='" + categoryName + "']"
        );
        wait.until(ExpectedConditions.elementToBeClickable(categoryLink)).click();
    }
}
