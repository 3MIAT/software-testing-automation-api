package com.automation.pages;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CatalogPage extends BasePage {
    private final By breadcrumbLast = By.cssSelector(".breadcrumb li:last-child");
    private final By leftMenuActive = By.cssSelector(".list-group a.active");
    private final By productPrices = By.cssSelector(".product-thumb .price");
    private final By productTitles = By.cssSelector(".product-layout h4 a");
    private final By sortDropdown = By.id("input-sort");
    private final By categoryDropdown = By.cssSelector("select[name='category_id']");
    private final By subCategoryCheckbox = By.cssSelector("input[name='sub_category']");
    private final By mainSearchButton = By.id("button-search");
    private final By noProductsMessage = By.xpath("//p[contains(.,'There is no product that matches the search criteria.')]");
    private final By successAlert = By.cssSelector(".alert.alert-success");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public void openDesktops() {
        openRoute("route=product/category&path=20");
    }

    public void openPhonesAndPdas() {
        openRoute("route=product/category&path=24");
    }

    public void openTablets() {
        openRoute("route=product/category&path=57");
    }

    public void openLaptops() {
        openRoute("route=product/category&path=18");
    }

    public void openMp3Players() {
        openRoute("route=product/category&path=34");
    }

    public void openTopCategory(String categoryText) {
        click(By.linkText(categoryText));
    }

    public void selectSortBy(String visibleText) {
        new Select(waitVisible(sortDropdown)).selectByVisibleText(visibleText);
    }

    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (WebElement element : visibleElements(productTitles)) {
            names.add(element.getText());
        }
        return names;
    }

    public void selectCategory(String categoryText) {
        new Select(waitVisible(categoryDropdown)).selectByVisibleText(categoryText);
    }

    public void checkSubCategorySearch() {
        WebElement checkbox = waitVisible(subCategoryCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clickMainSearchButton() {
        click(mainSearchButton);
    }

    public boolean isNoProductsMessageDisplayed() {
        return isDisplayed(noProductsMessage);
    }

    public String getBreadcrumbLastText() {
        return text(breadcrumbLast);
    }

    public String getLeftMenuActiveText() {
        return text(leftMenuActive);
    }

    public boolean pricesContainCurrency(String symbol) {
        List<WebElement> prices = driver.findElements(productPrices);
        if (prices.isEmpty()) {
            return false;
        }
        for (WebElement price : prices) {
            if (!price.getText().contains(symbol)) {
                return false;
            }
        }
        return true;
    }

    public void addProductToCart(String productName) {
        click(productCardButton(productName, "cart.add"));
    }

    public void openProductDetails(String productName) {
        click(By.xpath("//div[contains(@class,'product-thumb')]//h4/a[normalize-space()='" + productName + "']"));
    }

    public String getSuccessAlertText() {
        return text(successAlert);
    }

    private By productCardButton(String productName, String onclickFragment) {
        return By.xpath("//div[contains(@class,'product-thumb')][.//h4/a[normalize-space()='" + productName
                + "']]//button[contains(@onclick,'" + onclickFragment + "')]");
    }
}
