package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.CatalogPage;
import com.automation.pages.SearchPage;
import com.automation.utils.ExcelReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SearchAndSortTests extends BaseTest {
    @DataProvider(name = "tc07")
    public Object[][] tc07() throws IOException {
        return ExcelReader.readSheetAsMaps("TC07");
    }

    @DataProvider(name = "tc08")
    public Object[][] tc08() throws IOException {
        return ExcelReader.readSheetAsMaps("TC08");
    }

    @DataProvider(name = "tc09")
    public Object[][] tc09() throws IOException {
        return ExcelReader.readSheetAsMaps("TC09");
    }

    @Test(dataProvider = "tc07", description = "TC07 - Sort by name")
    public void tc07SortByName(Map<String, String> data) {
        loginAsConfiguredUser();

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.openPhonesAndPdas();

        catalogPage.selectSortBy(data.get("sortAscending"));
        assertSortedAscending(catalogPage.getProductNames());

        catalogPage.selectSortBy(data.get("sortDescending"));
        assertSortedDescending(catalogPage.getProductNames());
        catalogPage.logout();
    }

    @Test(dataProvider = "tc08", description = "TC08 - Search by product name")
    public void tc08SearchByName(Map<String, String> data) {
        loginAsConfiguredUser();

        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFromHeader(data.get("searchTerm"));

        CatalogPage catalogPage = new CatalogPage(driver);
        List<String> results = catalogPage.getProductNames();
        Assert.assertFalse(results.isEmpty(), "Search results should be displayed");
        for (String name : results) {
            Assert.assertTrue(name.toLowerCase().contains(data.get("searchTerm").toLowerCase()),
                    "Product name should contain the search term");
        }
        catalogPage.logout();
    }

    @Test(dataProvider = "tc09", description = "TC09 - Search in subcategories")
    public void tc09SearchInSubcategories(Map<String, String> data) {
        loginAsConfiguredUser();

        SearchPage searchPage = new SearchPage(driver);
        searchPage.openSearchPage();
        searchPage.enterSearchKeyword(data.get("searchTerm"));

        CatalogPage catalogPage = new CatalogPage(driver);
        catalogPage.selectCategory(data.get("category"));
        catalogPage.clickMainSearchButton();
        Assert.assertTrue(catalogPage.isNoProductsMessageDisplayed(), "No products message should be displayed first");

        catalogPage.checkSubCategorySearch();
        catalogPage.clickMainSearchButton();
        Assert.assertTrue(catalogPage.getProductNames().contains(data.get("expectedProduct")));
        catalogPage.logout();
    }

    private void assertSortedAscending(List<String> names) {
        Assert.assertFalse(names.isEmpty(), "Products should be displayed");
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                    "Products should be sorted A-Z");
        }
    }

    private void assertSortedDescending(List<String> names) {
        Assert.assertFalse(names.isEmpty(), "Products should be displayed");
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) >= 0,
                    "Products should be sorted Z-A");
        }
    }
}
