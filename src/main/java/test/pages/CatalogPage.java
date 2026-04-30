package test.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import test.config.Config;

public class CatalogPage extends BasePage {
    private final By breadcrumbLast = By.cssSelector(".breadcrumb li:last-child");
    private final By leftMenuActive = By.cssSelector(".list-group a.active");
    private final By productPrices = By.cssSelector(".product-thumb .price");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public void openTopCategory(String categoryText) {
        By categoryLink = By.linkText(categoryText);
        waitVisible(categoryLink).click();
    }

    public void openShowAllDesktops() {
        String baseUrl = Config.get("baseUrl");
        String targetUrl = baseUrl.replace("route=common/home", "route=product/category&path=20");
        driver.get(targetUrl);
    }

    public String getBreadcrumbLastText() {
        return waitVisible(breadcrumbLast).getText();
    }

    public String getLeftMenuActiveText() {
        return waitVisible(leftMenuActive).getText();
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
}
