package com.automation.base;

import com.automation.config.ConfigReader;
import com.automation.core.DriverFactory;
import com.automation.pages.AccountPage;
import com.automation.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        driver.get(ConfigReader.getHomeUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected void loginAsConfiguredUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(ConfigReader.get("email"), ConfigReader.get("password"));

        if (loginPage.isWarningDisplayed()) {
            registerNewUser();
        }
    }

    protected void logoutIfPossible() {
        AccountPage accountPage = new AccountPage(driver);
        if (accountPage.isLogoutVisible()) {
            accountPage.logout();
        }
    }

    private void registerNewUser() {
        driver.get(ConfigReader.getHomeUrl());
        AccountPage accountPage = new AccountPage(driver);
        accountPage.openMyAccountRegister();
        accountPage.enterFirstName("Auto");
        accountPage.enterLastName("User");
        accountPage.enterEmail("auto_user_" + System.currentTimeMillis() + "@example.com");
        accountPage.enterTelephone("0123456789");
        accountPage.enterPassword(ConfigReader.get("password", "Pass1234"));
        accountPage.enterConfirmPassword(ConfigReader.get("password", "Pass1234"));
        accountPage.setAgree(true);
        accountPage.submitRegistration();
    }
}
