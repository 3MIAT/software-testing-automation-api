package com.automation.pages;

import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {
    private final By cartTotalText = By.id("cart-total");
    private final By cartRows = By.cssSelector("#content form .table-responsive table.table-bordered tbody tr");
    private final By totalRows = By.cssSelector("#content .col-sm-4.col-sm-offset-8 table.table-bordered tr");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void openViewCart() {
        openRoute("route=checkout/cart");
    }

    public void openCheckout() {
        openRoute("route=checkout/checkout");
    }

    public void clickCheckoutFromCartPage() {
        click(By.linkText("Checkout"));
    }

    public boolean containsItem(String itemName) {
        for (WebElement row : cartItemRows()) {
            if (row.getText().contains(itemName)) {
                return true;
            }
        }
        return false;
    }

    public boolean itemRowContains(String itemName, String expectedText) {
        for (WebElement row : cartItemRows()) {
            if (row.getText().contains(itemName)) {
                return row.getText().contains(expectedText);
            }
        }
        return false;
    }

    public BigDecimal getDisplayedGrandTotal() {
        for (WebElement row : visibleElements(totalRows)) {
            if ("Total:".equals(row.findElement(By.cssSelector("td:first-child")).getText().trim())) {
                return parseMoney(row.findElement(By.cssSelector("td:nth-child(2)")).getText());
            }
        }
        throw new IllegalStateException("Grand total row was not displayed");
    }

    public BigDecimal sumLineTotals() {
        BigDecimal total = BigDecimal.ZERO;
        for (WebElement row : cartItemRows()) {
            List<WebElement> columns = row.findElements(By.cssSelector("td"));
            if (columns.size() >= 6) {
                total = total.add(parseMoney(columns.get(5).getText()));
            }
        }
        return total;
    }

    public String getSmallCartText() {
        return text(cartTotalText);
    }

    private List<WebElement> cartItemRows() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(cartRows, 0));
        return driver.findElements(cartRows);
    }

    private BigDecimal parseMoney(String value) {
        String normalized = value.replaceAll("[^0-9.]", "");
        return normalized.isBlank() ? BigDecimal.ZERO : new BigDecimal(normalized);
    }
}
