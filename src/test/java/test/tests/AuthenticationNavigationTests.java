package test.tests;

import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.pages.AccountPage;
import test.pages.CatalogPage;
import test.pages.LoginPage;
import test.utils.ExcelUtils;

public class AuthenticationNavigationTests extends BaseTest {

    @DataProvider(name = "tc01")
    public Object[][] tc01() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC01");
    }

    @DataProvider(name = "tc02")
    public Object[][] tc02() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC02");
    }

    @DataProvider(name = "tc03")
    public Object[][] tc03() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC03");
    }

    @DataProvider(name = "tc04")
    public Object[][] tc04() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC04");
    }

    @DataProvider(name = "tc05")
    public Object[][] tc05() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC05");
    }

    @DataProvider(name = "tc06")
    public Object[][] tc06() throws IOException {
        return ExcelUtils.readSheetAsMaps("TC06");
    }

    @Test(dataProvider = "tc01", priority = 1)
    public void tc01_registration_without_errors(Map<String, String> data) {
        AccountPage accountPage = new AccountPage(driver);
        accountPage.openMyAccountRegister();

        accountPage.enterFirstName(data.get("firstName"));
        accountPage.enterLastName(data.get("lastName"));
        accountPage.enterEmail(data.get("email"));
        accountPage.enterTelephone(data.get("telephone"));
        accountPage.enterPassword(data.get("password"));
        accountPage.enterConfirmPassword(data.get("confirmPassword"));
        accountPage.setAgree(true);
        accountPage.submitRegistration();

        Assert.assertEquals(accountPage.getSuccessHeading(), "Your Account Has Been Created!");
        Assert.assertTrue(accountPage.isLogoutVisible(), "Logout should be visible in My Account menu");
        accountPage.logout();
    }

    @Test(dataProvider = "tc02", priority = 2)
    public void tc02_registration_with_errors(Map<String, String> data) {
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

    @Test(dataProvider = "tc03", priority = 3)
    public void tc03_valid_login(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(data.get("email"), data.get("password"));

        AccountPage accountPage = new AccountPage(driver);
        Assert.assertEquals(accountPage.getAccountHeader(), "My Account");
        accountPage.logout();
    }

    @Test(dataProvider = "tc04", priority = 4)
    public void tc04_invalid_login(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(data.get("email"), data.get("password"));

        Assert.assertTrue(loginPage.getWarningMessage().contains("No match for E-Mail Address and/or Password."));
    }

    @Test(dataProvider = "tc05", priority = 5)
    public void tc05_change_currency(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(data.get("email"), data.get("password"));

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openShowAllDesktops();
        Assert.assertTrue(catalogPage.pricesContainCurrency("$"));

        catalogPage.changeCurrency("EUR");
        Assert.assertTrue(catalogPage.pricesContainCurrency("€"));
        catalogPage.logout();
    }

    @Test(dataProvider = "tc06", priority = 6)
    public void tc06_breadcrumb_and_left_menu(Map<String, String> data) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openMyAccountLogin();
        loginPage.login(data.get("email"), data.get("password"));

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openTopCategory("Tablets");

        Assert.assertTrue(catalogPage.getBreadcrumbLastText().startsWith("Tablets"));
        Assert.assertTrue(catalogPage.getLeftMenuActiveText().startsWith("Tablets"));
        catalogPage.logout();
    }
}
