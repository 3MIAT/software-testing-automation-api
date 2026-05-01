package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends BasePage {

    private By searchInput = By.cssSelector("#search input");
    private By searchButton = By.cssSelector("#search button");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public void enterSearchTermAndSubmit(String term) {
        enterText(searchInput, term);
        click(searchButton);
    }
}
