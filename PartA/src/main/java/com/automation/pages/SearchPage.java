package com.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends BasePage {
    private final By headerSearchInput = By.cssSelector("#search input[name='search']");
    private final By headerSearchButton = By.cssSelector("#search button");
    private final By searchKeywordInput = By.id("input-search");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public void searchFromHeader(String term) {
        type(headerSearchInput, term);
        click(headerSearchButton);
    }

    public void enterSearchKeyword(String term) {
        type(searchKeywordInput, term);
    }

    public void openSearchPage() {
        click(headerSearchButton);
    }
}
