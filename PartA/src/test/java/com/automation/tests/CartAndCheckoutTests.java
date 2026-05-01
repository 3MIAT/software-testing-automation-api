package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.CartPage;
import com.automation.pages.CatalogPage;
import com.automation.pages.CheckoutPage;
import com.automation.pages.ProductPage;
import com.automation.utils.ExcelReader;
import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CartAndCheckoutTests extends BaseTest {
    @DataProvider(name = "tc10")
    public Object[][] tc10() throws IOException {
        return ExcelReader.readSheetAsMaps("TC10");
    }

    @DataProvider(name = "tc11")
    public Object[][] tc11() throws IOException {
        return ExcelReader.readSheetAsMaps("TC11");
    }

    @Test(dataProvider = "tc10", description = "TC10 - Add items to shopping cart and compare")
    public void tc10AddItemsToCart(Map<String, String> data) {
        loginAsConfiguredUser();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openTablets();
        catalogPage.addProductToCart(data.get("tabletProduct"));
        Assert.assertTrue(catalogPage.getSuccessAlertText().contains(data.get("tabletProduct")));

        CartPage cartPage = new CartPage(driver);
        cartPage.openViewCart();
        Assert.assertTrue(cartPage.containsItem(data.get("tabletProduct")));

        catalogPage.openLaptops();
        catalogPage.openProductDetails(data.get("laptopProduct"));
        ProductPage productPage = new ProductPage(driver);
        productPage.setDeliveryDate(data.get("deliveryDate"));
        productPage.addToCart();
        Assert.assertTrue(productPage.getSuccessAlertText().contains(data.get("laptopProduct")));

        cartPage.openViewCart();
        Assert.assertTrue(cartPage.containsItem(data.get("tabletProduct")));
        Assert.assertTrue(cartPage.containsItem(data.get("laptopProduct")));
        Assert.assertTrue(cartPage.itemRowContains(data.get("laptopProduct"), data.get("deliveryDate")));
        Assert.assertEquals(cartPage.getDisplayedGrandTotal(), cartPage.sumLineTotals());
        logoutIfPossible();
    }

    @Test(dataProvider = "tc11", description = "TC11 - Normal checkout process and confirm order")
    public void tc11CheckoutProcess(Map<String, String> data) {
        loginAsConfiguredUser();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openMp3Players();
        catalogPage.addProductToCart(data.get("product"));
        Assert.assertTrue(catalogPage.getSuccessAlertText().contains(data.get("product")));

        CartPage cartPage = new CartPage(driver);
        cartPage.openViewCart();
        Assert.assertTrue(cartPage.containsItem(data.get("product")));
        cartPage.clickCheckoutFromCartPage();

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.continueBillingWithNewAddress(
                data.get("firstName"),
                data.get("lastName"),
                data.get("address"),
                data.get("city"),
                data.get("postCode")
        );
        checkoutPage.continueShippingAddress();
        checkoutPage.enterDeliveryComment(data.get("deliveryComment"));
        checkoutPage.continueDeliveryMethod();
        checkoutPage.acceptTermsAndConditions();
        checkoutPage.continuePaymentMethod();
        checkoutPage.confirmOrder();

        Assert.assertTrue(checkoutPage.getSuccessMessage().contains("Your order has been placed!"));
        Assert.assertTrue(cartPage.getSmallCartText().contains("0 item(s)"));
        logoutIfPossible();
    }
}
