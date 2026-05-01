package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.AccountPage;
import com.automation.pages.CatalogPage;
import com.automation.pages.LoginPage;
import com.automation.utils.ExcelReader;
import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuthenticationNavigationTests extends BaseTest {
    private static String registeredEmail;
    private static String registeredPassword;

    @DataProvider(name = "tc01")
    public Object[][] tc01() throws IOException {
        return ExcelReader.readSheetAsMaps("TC01");
    }

    @DataProvider(name = "tc02")
    public Object[][] tc02() throws IOException {
        return ExcelReader.readSheetAsMaps("TC02");
    }

    @DataProvider(name = "tc03")
    public Object[][] tc03() throws IOException {
        return ExcelReader.readSheetAsMaps("TC03");
    }

    @DataProvider(name = "tc04")
    public Object[][] tc04() throws IOException {
        return ExcelReader.readSheetAsMaps("TC04");
    }

    @DataProvider(name = "tc05")
    public Object[][] tc05() throws IOException {
        return ExcelReader.readSheetAsMaps("TC05");
    }

    @DataProvider(name = "tc06")
    public Object[][] tc06() throws IOException {
        return ExcelReader.readSheetAsMaps("TC06");
    }

    @Test(dataProvider = "tc01", priority = 1, description = "TC01 - Registration without errors")
    public void tc01RegistrationWithoutErrors(Map<String, String> data) {
        AccountPage accountPage = new AccountPage(driver);
        accountPage.openMyAccountRegister();

        String email = uniqueEmail(data.get("email"));
        registeredEmail = email;
        registeredPassword = data.get("password");

        accountPage.enterFirstName(data.get("firstName"));
        accountPage.enterLastName(data.get("lastName"));
        accountPage.enterEmail(email);
        accountPage.enterTelephone(data.get("telephone"));
        accountPage.enterPassword(data.get("password"));
        accountPage.enterConfirmPassword(data.get("confirmPassword"));
        accountPage.setAgree(true);
        accountPage.submitRegistration();

        Assert.assertEquals(accountPage.getSuccessHeading(), "Your Account Has Been Created!");
        Assert.assertTrue(accountPage.isLogoutVisible(), "Logout should be visible in the My Account menu");
        accountPage.logout();
    }

    @Test(dataProvider = "tc02", priority = 2, description = "TC02 - Registration with errors")
    public void tc02RegistrationWithErrors(Map<String, String> data) {
        AccountPage accountPage = new AccountPage(driver);
        accountPage.openMyAccountRegister();

        accountPage.enterFirstName(data.get("firstName"));
        accountPage.enterLastName(data.get("lastName"));
        accountPage.submitRegistration();

        Assert.assertEquals(accountPage.getFieldErrorByInputId("input-email"), "E-Mail Address does not appear to be valid!");
        Assert.assertEquals(accountPage.getFieldErrorByInputId("input-telephone"), "Telephone must be between 3 and 32 characters!");
        Assert.assertEquals(accountPage.getFieldErrorByInputId("input-password"), "Password must be between 4 and 20 characters!");

        accountPage.enterEmail(data.get("email"));
        accountPage.enterTelephone(data.get("telephone"));
        accountPage.enterPassword(data.get("shortPassword"));
        accountPage.enterConfirmPassword(data.get("shortPassword"));
        accountPage.setAgree(true);
        accountPage.submitRegistration();

        Assert.assertEquals(accountPage.getPasswordErrorMessage(), "Password must be between 4 and 20 characters!");
    }

    @Test(dataProvider = "tc03", priority = 3, description = "TC03 - Valid login")
    public void tc03ValidLogin(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(resolveLoginEmail(data.get("email")), resolveLoginPassword(data.get("password")));

        AccountPage accountPage = new AccountPage(driver);
        Assert.assertEquals(accountPage.getAccountHeader(), "My Account");
        accountPage.logout();
    }

    @Test(dataProvider = "tc04", priority = 4, description = "TC04 - Invalid login")
    public void tc04InvalidLogin(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(data.get("email"), data.get("password"));

        Assert.assertTrue(loginPage.getWarningMessage().contains("No match for E-Mail Address and/or Password."));
    }

    @Test(dataProvider = "tc05", priority = 5, description = "TC05 - Change currency")
    public void tc05ChangeCurrency(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(resolveLoginEmail(data.get("email")), resolveLoginPassword(data.get("password")));

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openDesktops();
        Assert.assertTrue(catalogPage.pricesContainCurrency("$"));

        catalogPage.changeCurrency("EUR");
        Assert.assertTrue(catalogPage.pricesContainCurrency("\u20ac"));
        catalogPage.logout();
    }

    @Test(dataProvider = "tc06", priority = 6, description = "TC06 - Breadcrumb and left menu")
    public void tc06BreadcrumbAndLeftMenu(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(resolveLoginEmail(data.get("email")), resolveLoginPassword(data.get("password")));

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openTopCategory("Tablets");

        Assert.assertTrue(catalogPage.getBreadcrumbLastText().startsWith("Tablets"));
        Assert.assertTrue(catalogPage.getLeftMenuActiveText().startsWith("Tablets"));
        catalogPage.logout();
    }

    private static String uniqueEmail(String email) {
        String base = (email == null || email.isBlank()) ? "auto_user@example.com" : email.trim();
        int atIndex = base.indexOf('@');
        String stamp = String.valueOf(System.currentTimeMillis());
        if (atIndex <= 0) {
            return "auto_user_" + stamp + "@example.com";
        }
        return base.substring(0, atIndex) + "+" + stamp + base.substring(atIndex);
    }

    private static String resolveLoginEmail(String fallbackEmail) {
        return registeredEmail != null ? registeredEmail : fallbackEmail;
    }

    private static String resolveLoginPassword(String fallbackPassword) {
        return registeredPassword != null ? registeredPassword : fallbackPassword;
    }
}
