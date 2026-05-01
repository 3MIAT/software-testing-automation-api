# Checkout Tutorial & Documentation (Member 3 Tasks)

Welcome to the **checkout branch**! This tutorial will walk you through everything that was created for Member 3's portion of Assignment 2, including the implementation of Test Cases 07 through 11 and their respective Page Object Models.

## Overview of Tasks

1.  **POMs Created:**
    *   `CartPage.java` (Managing shopping cart, validating totals)
    *   `CheckoutPage.java` (Navigating the multi-step accordion Checkout process)
    *   `SearchPage.java` & `CategoryPage.java` (To fulfill the Search and Sort Test Cases TC-07, TC-08, TC-09)
2.  **Test Classes Created:**
    *   `SearchAndSortTests.java`
    *   `CartAndCheckoutTests.java`
3.  **Framework Foundation:**
    *   Maven project setup (`pom.xml` with dependencies for Selenium, TestNG, Apache POI, Allure).
    *   Base framework (`BaseTest.java`, `BasePage.java`, `ConfigReader.java`, `ExcelReader.java`).

## Step-by-Step Guide

### 1. Framework Base & Configuration
We structured the automation framework according to the Page Object Model (POM) pattern.
*   **`ConfigReader.java`**: Reads the base URL and browser configuration from `src/main/resources/config.properties`.
*   **`BaseTest.java`**: Initializes the `WebDriver` before each test (`@BeforeMethod`) and cleans it up after (`@AfterMethod`). It also attaches screenshots for Allure in case of test failures.

### 2. Page Object Model (POM) Implementation
Instead of throwing locators inside tests, we encapsulate them in their own classes.

#### Search and Sort (TC-07 to TC-09)
*   **`CategoryPage.java`**: Handles the `Sort By` dropdown. We locate the dropdown using its ID `input-sort`, then select options like "Name (A - Z)" and extract all the displayed product names to verify they are sorted correctly.
*   **`SearchPage.java`**: Uses CSS selectors to find the top search bar (`#search input`). It handles searching for terms like "Mac" and interacting with the "Search in subcategories" checkbox (`input[name='sub_category']`).

#### Cart & Checkout (TC-10 to TC-11)
*   **`CartPage.java`**: We use CSS selectors like `#cart-total` to open the cart preview and `.table-bordered` to read items directly from the main cart page. We validate prices by extracting the strings, parsing them to `Double`, and comparing them.
*   **`CheckoutPage.java`**: This is the most complex page because of the accordion UI.
    *   **Billing Details:** `#collapse-payment-address`
    *   **Shipping Details:** `#collapse-shipping-address`
    *   **Delivery Method:** `#collapse-shipping-method`
    *   **Payment Method:** `#collapse-payment-method` (Contains the Terms & Conditions checkbox `input[name='agree']`).
    *   **Confirm Order:** `#collapse-checkout-confirm`
    *   The flow requires explicit waits (`WebDriverWait`) between clicking "Continue" in each section to wait for the next accordion body to expand and become visible/interactable.

### 3. Data-Driven Testing (Excel)
We use `Apache POI` to read from the provided `A2_PartA_Test Scenarios.xlsx` sheet. The `ExcelReader` utility fetches data, which we then pass into TestNG's `@DataProvider`. This ensures we are not hardcoding things like product names, search terms, or billing addresses directly in our code.

### 4. Running the Tests

1.  **Open your Terminal/Command Prompt** at the root of the project.
2.  **Execute the Maven Test Command:**
    ```bash
    mvn clean test
    ```
3.  **Generate and View Allure Report:**
    Once tests are finished, Allure results are saved in the `target/allure-results` folder.
    ```bash
    mvn allure:serve
    ```
    This will compile the results and open an interactive HTML report in your default web browser!

## Conclusion
You now have a fully functional `feat(checkout)` branch complete with the required tests, POMs, and data-driven setup for Member 3's assignment scope.