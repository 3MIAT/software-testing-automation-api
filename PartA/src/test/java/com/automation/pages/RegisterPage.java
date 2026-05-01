package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ──────────────────────────────────────────────
    private final By firstNameField    = By.id("input-firstname");
    private final By lastNameField     = By.id("input-lastname");
    private final By emailField        = By.id("input-email");
    private final By phoneField        = By.id("input-telephone");
    private final By passwordField     = By.id("input-password");
    private final By confirmPassField  = By.id("input-confirm");
    private final By newsletterYes     = By.xpath("//input[@name='newsletter'][@value='1']");
    private final By newsletterNo      = By.xpath("//input[@name='newsletter'][@value='0']");
    private final By privacyCheckbox   = By.name("agree");
    private final By submitButton      = By.cssSelector("input[value='Continue']");
    private final By successHeading    = By.cssSelector("div#content h1");
    private final By errorAlert        = By.cssSelector("div.alert-danger");
    private final By fieldErrors       = By.cssSelector("div.text-danger");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Actions ───────────────────────────────────────────────

    public void enterFirstName(String firstName) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        el.clear();
        el.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        WebElement el = driver.findElement(lastNameField);
        el.clear();
        el.sendKeys(lastName);
    }

    public void enterEmail(String email) {
        WebElement el = driver.findElement(emailField);
        el.clear();
        el.sendKeys(email);
    }

    public void enterPhone(String phone) {
        WebElement el = driver.findElement(phoneField);
        el.clear();
        el.sendKeys(phone);
    }

    public void enterPassword(String password) {
        WebElement el = driver.findElement(passwordField);
        el.clear();
        el.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement el = driver.findElement(confirmPassField);
        el.clear();
        el.sendKeys(confirmPassword);
    }

    public void selectNewsletter(boolean subscribe) {
        if (subscribe) {
            driver.findElement(newsletterYes).click();
        } else {
            driver.findElement(newsletterNo).click();
        }
    }

    public void agreeToPrivacyPolicy() {
        WebElement checkbox = driver.findElement(privacyCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickSubmit() {
        driver.findElement(submitButton).click();
    }

    // ── Full registration flow ─────────────────────────────────

    public void registerUser(String firstName, String lastName, String email,
                             String phone, String password, boolean newsletter) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(password);
        selectNewsletter(newsletter);
        agreeToPrivacyPolicy();
        clickSubmit();
    }

    // ── Assertions ────────────────────────────────────────────

    public boolean isRegistrationSuccessful() {
        try {
            String heading = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(successHeading)
            ).getText();
            return heading.contains("Your Account Has Been Created");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorAlertDisplayed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(errorAlert)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getFieldErrors() {
        return driver.findElements(fieldErrors)
                .stream()
                .map(WebElement::getText)
                .filter(t -> !t.isEmpty())
                .collect(Collectors.toList());
    }

    public String getErrorAlertText() {
        return driver.findElement(errorAlert).getText();
    }
}
