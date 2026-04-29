package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class CategoryPage extends BasePage {

    private By sortDropdown = By.id("input-sort");
    private By productTitles = By.cssSelector(".product-layout h4 a");
    private By subCategoryCheckbox = By.cssSelector("input[name='sub_category']");
    private By mainSearchButton = By.id("button-search");
    private By categoryDropdown = By.cssSelector("select[name='category_id']");
    private By noProductsMessage = By.xpath("//p[contains(text(),'There is no product that matches the search criteria.')]");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public void selectSortBy(String visibleText) {
        WebElement dropdown = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(sortDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(visibleText);
    }

    public List<String> getProductNames() {
        List<WebElement> titleElements = getElements(productTitles);
        List<String> names = new ArrayList<>();
        for (WebElement element : titleElements) {
            names.add(element.getText());
        }
        return names;
    }

    public void selectCategory(String categoryText) {
        WebElement dropdown = wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(categoryDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(categoryText);
    }

    public void checkSubCategorySearch() {
        if (!driver.findElement(subCategoryCheckbox).isSelected()) {
            click(subCategoryCheckbox);
        }
    }

    public void clickMainSearchButton() {
        click(mainSearchButton);
    }
    
    public boolean isNoProductsMessageDisplayed() {
        return isElementDisplayed(noProductsMessage);
    }
}
