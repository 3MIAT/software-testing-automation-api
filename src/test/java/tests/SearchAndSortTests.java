package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CategoryPage;
import pages.SearchPage;
import utils.ExcelReader;

import java.util.List;

public class SearchAndSortTests extends BaseTest {

    @DataProvider(name = "searchData")
    public Object[][] searchData() {
        return new Object[][]{
                {"Mac", true}, // Term, expectResults
                {"Apple", false} // We will use this for subcategory logic
        };
    }

    @Test(description = "TC-07: Sort By name")
    public void testSortByName_TC07() {
        // Navigate to Phones & PDAs
        driver.get("http://tutorialsninja.com/demo/index.php?route=product/category&path=24");
        
        CategoryPage categoryPage = new CategoryPage(driver);
        
        // Sort A-Z
        categoryPage.selectSortBy("Name (A - Z)");
        List<String> namesAZ = categoryPage.getProductNames();
        Assert.assertTrue(namesAZ.size() > 0, "Products should be displayed");
        
        // Very basic validation (ensure it sorts correctly)
        for (int i = 0; i < namesAZ.size() - 1; i++) {
            Assert.assertTrue(namesAZ.get(i).compareToIgnoreCase(namesAZ.get(i + 1)) <= 0, 
                "List is not sorted alphabetically Ascending");
        }

        // Sort Z-A
        categoryPage.selectSortBy("Name (Z - A)");
        List<String> namesZA = categoryPage.getProductNames();
        
        for (int i = 0; i < namesZA.size() - 1; i++) {
            Assert.assertTrue(namesZA.get(i).compareToIgnoreCase(namesZA.get(i + 1)) >= 0, 
                "List is not sorted alphabetically Descending");
        }
    }

    @Test(description = "TC-08: Search by name", dataProvider = "searchData")
    public void testSearchByName_TC08(String searchTerm, boolean expectResults) {
        if (!expectResults) return; // Skip the Apple one for this test

        SearchPage searchPage = new SearchPage(driver);
        searchPage.enterSearchTermAndSubmit(searchTerm);
        
        CategoryPage categoryPage = new CategoryPage(driver);
        List<String> results = categoryPage.getProductNames();
        Assert.assertTrue(results.size() > 0, "Search results should be displayed");
        for (String name : results) {
            Assert.assertTrue(name.toLowerCase().contains(searchTerm.toLowerCase()), "Result does not contain search term");
        }
    }

    @Test(description = "TC-09: Search in subcategories")
    public void testSearchInSubcategories_TC09() {
        SearchPage searchPage = new SearchPage(driver);
        searchPage.enterSearchTermAndSubmit("Apple");

        CategoryPage categoryPage = new CategoryPage(driver);
        
        // Select Components from dropdown
        categoryPage.selectCategory("Components");
        categoryPage.clickMainSearchButton();
        
        // Validate no products found initially
        Assert.assertTrue(categoryPage.isNoProductsMessageDisplayed(), "No products message should be displayed");
        
        // Check "Search in subcategories"
        categoryPage.checkSubCategorySearch();
        categoryPage.clickMainSearchButton();

        // Validate "Apple Cinema 30" displayed
        List<String> results = categoryPage.getProductNames();
        boolean found = false;
        for (String name : results) {
            if (name.contains("Apple Cinema 30")) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found, "'Apple Cinema 30' should be displayed after searching in subcategories");
    }
}
