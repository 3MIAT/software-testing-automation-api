# Assignment 2 - Part A UI Automation README

This folder contains the UI automation framework for **Assignment 2 - Part A**. The project automates test scenarios against the TutorialsNinja demo web application:

```text
https://tutorialsninja.com/demo/
```

The framework is written in **Java** and uses:

- **Maven** to manage dependencies and run tests.
- **Selenium WebDriver** to control a real browser.
- **TestNG** to define tests, test suites, priorities, assertions, and data providers.
- **Page Object Model (POM)** to keep page-specific Selenium code separate from test logic.
- **Apache POI** to read test data from Excel.
- **Allure** to generate readable test reports and attach screenshots on failures.
- **WebDriverManager** to download and configure browser drivers automatically.

The goal of this README is to explain the project from zero: what every important folder does, how the code flows, what the Java/TestNG/Selenium syntax means, how data is read, how tests run, and how to add or debug tests.

---

## 1. Project Location

Part A lives here:

```text
software-testing-automation-api/
`-- PartA/
```

All commands in this README should be executed from:

```powershell
cd "software-testing-automation-api\PartA"
```

---

## 2. What This Project Tests

The automated tests cover 11 UI scenarios:

| Test Case | Area | What it checks |
|---|---|---|
| TC01 | Registration | A user can register successfully with valid data. |
| TC02 | Registration validation | Required/invalid fields show the expected validation messages. |
| TC03 | Login | A valid user can log in. |
| TC04 | Login validation | Invalid credentials show an error warning. |
| TC05 | Currency | Product prices update when currency is changed. |
| TC06 | Navigation | Breadcrumb and left menu reflect the selected category. |
| TC07 | Sorting | Products can be sorted A-Z and Z-A. |
| TC08 | Search | Header search returns products matching the search term. |
| TC09 | Search in subcategories | Searching with and without subcategories behaves correctly. |
| TC10 | Cart | Multiple products can be added and cart totals are verified. |
| TC11 | Checkout | A normal checkout flow can place an order. |

---

## 3. Prerequisites

Install these before running the project:

### Java 17

The project is configured for Java 17 in `pom.xml`:

```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

Check your Java version:

```powershell
java -version
```

### Maven

Maven downloads dependencies and runs the TestNG suite.

Check Maven:

```powershell
mvn -version
```

### Browser

The default browser is Chrome:

```properties
browser=chrome
```

Chrome should be installed locally. WebDriverManager handles the matching ChromeDriver automatically.

---

## 4. How to Run

From the `PartA` folder:

```powershell
mvn test
```

This command:

1. Reads `pom.xml`.
2. Downloads dependencies if needed.
3. Uses Maven Surefire to run TestNG.
4. TestNG reads `testng.xml`.
5. The listed test classes execute.
6. Browser sessions open, run, and close.
7. Results are written under `target/`.
8. Allure raw results are written under `target/allure-results`.

Generate an Allure report:

```powershell
mvn allure:report
```

Serve/open the Allure report locally:

```powershell
mvn allure:serve
```

Useful cleanup command:

```powershell
mvn clean test
```

`clean` deletes old build/test output before running again.

---

## 5. Main Folder Structure

```text
PartA/
|-- pom.xml
|-- testng.xml
|-- docs/
|   `-- assignment-summary.md
|-- src/
|   |-- main/
|   |   `-- java/
|   |       `-- com/
|   |           `-- automation/
|   |               |-- config/
|   |               |   `-- ConfigReader.java
|   |               |-- core/
|   |               |   `-- DriverFactory.java
|   |               |-- pages/
|   |               |   |-- BasePage.java
|   |               |   |-- AccountPage.java
|   |               |   |-- LoginPage.java
|   |               |   |-- CatalogPage.java
|   |               |   |-- SearchPage.java
|   |               |   |-- ProductPage.java
|   |               |   |-- CartPage.java
|   |               |   `-- CheckoutPage.java
|   |               `-- utils/
|   |                   `-- ExcelReader.java
|   `-- test/
|       |-- java/
|       |   `-- com/
|       |       `-- automation/
|       |           |-- base/
|       |           |   `-- BaseTest.java
|       |           |-- listeners/
|       |           |   `-- AllureListener.java
|       |           |-- tests/
|       |           |   |-- AuthenticationNavigationTests.java
|       |           |   |-- SearchAndSortTests.java
|       |           |   `-- CartAndCheckoutTests.java
|       |           `-- utils/
|       |               `-- ScreenshotUtils.java
|       `-- resources/
|           |-- config.properties
|           |-- allure.properties
|           `-- testdata/
|               |-- testdata.xlsx
|               `-- README.txt
`-- target/
```

### `src/main/java`

This contains reusable framework and page code. It is not the test layer itself.

### `src/test/java`

This contains the actual TestNG test classes, test setup/teardown code, listeners, and test-only utilities.

### `src/test/resources`

This contains files loaded at runtime:

- `config.properties`
- `allure.properties`
- `testdata/testdata.xlsx`

Maven automatically places test resources on the test classpath, which is why Java code can load them without using full file paths.

### `target`

This is generated by Maven. You do not write code here. It contains compiled classes, TestNG reports, Surefire reports, and Allure result files.

---

## 6. Build File: `pom.xml`

`pom.xml` is the Maven project file. It defines the project name, Java version, dependencies, and plugins.

Important project identity:

```xml
<groupId>com.automation</groupId>
<artifactId>software-testing-partA</artifactId>
<version>1.0-SNAPSHOT</version>
```

Important dependencies:

| Dependency | Purpose |
|---|---|
| `selenium-java` | Controls browser actions such as click, type, find element, and read text. |
| `webdrivermanager` | Downloads/configures browser drivers automatically. |
| `testng` | Provides `@Test`, `@DataProvider`, assertions, priorities, and suite execution. |
| `allure-testng` | Connects TestNG results to Allure reporting. |
| `poi` and `poi-ooxml` | Read `.xlsx` Excel data files. |
| `slf4j-simple` | Simple logging implementation used by dependencies. |
| `aspectjweaver` | Runtime agent used by Allure integration. |

The Maven Surefire plugin is configured to run `testng.xml`:

```xml
<suiteXmlFiles>
    <suiteXmlFile>testng.xml</suiteXmlFile>
</suiteXmlFiles>
```

That means `mvn test` does not randomly discover all tests. It follows the suite file.

---

## 7. Test Suite File: `testng.xml`

`testng.xml` tells TestNG which tests to run and in what suite structure.

Current suite:

```xml
<suite name="Assignment 2 - Part A UI Automation" verbose="1" parallel="none">
```

Meaning:

- `name` is the display name of the suite.
- `verbose="1"` controls logging detail.
- `parallel="none"` means tests run sequentially, not in parallel.

The listener is registered here:

```xml
<listeners>
    <listener class-name="com.automation.listeners.AllureListener"/>
</listeners>
```

This makes TestNG call `AllureListener` during test events, especially failures.

The suite contains three logical groups:

```xml
<test name="Registration, Authentication, and Navigation">
    <classes>
        <class name="com.automation.tests.AuthenticationNavigationTests"/>
    </classes>
</test>
```

Each `<class>` points to a Java test class by its full package name.

If you create a new test class and want it to run with `mvn test`, add it to `testng.xml`.

---

## 8. Configuration File: `config.properties`

Path:

```text
src/test/resources/config.properties
```

Current values:

```properties
browser=chrome
headless=false

base.url=https://tutorialsninja.com/demo/
implicit.wait=10
explicit.wait=15

email=testuser123_qas@test.com
password=testpassword123
screenshot.on.failure=true
```

What each property means:

| Key | Meaning |
|---|---|
| `browser` | Browser to run: `chrome`, `firefox`, `edge`, or `chromium`. |
| `headless` | `true` runs Chrome without a visible window. `false` shows the browser. |
| `base.url` | Base URL of the TutorialsNinja application. |
| `implicit.wait` | Default Selenium implicit wait in seconds. |
| `explicit.wait` | Default explicit wait used by page objects. |
| `email` | Default login email used by helper login flows. |
| `password` | Default login password used by helper login flows. |
| `screenshot.on.failure` | If `true`, failed tests attach screenshots to Allure. |

Example headless run configuration:

```properties
headless=true
```

---

## 9. Configuration Reader: `ConfigReader.java`

Path:

```text
src/main/java/com/automation/config/ConfigReader.java
```

This class loads `config.properties` once when the class is first used:

```java
private static final Properties PROPERTIES = new Properties();
```

The static block runs automatically when Java loads the class:

```java
static {
    try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
        ...
        PROPERTIES.load(input);
    }
}
```

Important syntax:

- `static` means the code belongs to the class itself, not to an object instance.
- `try (...) { ... }` is Java try-with-resources. It closes the input stream automatically.
- `getClassLoader().getResourceAsStream(...)` loads files from `src/test/resources` after Maven places them on the classpath.

Common methods:

```java
ConfigReader.get("email")
ConfigReader.get("password", "Pass1234")
ConfigReader.getInt("explicit.wait", 15)
ConfigReader.getBoolean("headless", false)
ConfigReader.getBrowser()
ConfigReader.getHomeUrl()
ConfigReader.getRouteUrl("route=account/login")
```

The overloads with a default value protect the code when a property is missing:

```java
get("password", "Pass1234")
```

This means: return the configured password if it exists, otherwise return `Pass1234`.

---

## 10. Browser Creation: `DriverFactory.java`

Path:

```text
src/main/java/com/automation/core/DriverFactory.java
```

`DriverFactory` is responsible for creating Selenium `WebDriver` objects.

Main method:

```java
public static WebDriver createDriver()
```

Flow:

1. Read the browser name from `config.properties`.
2. Normalize it with `.trim().toLowerCase()`.
3. Choose the correct browser using `switch`.
4. Ask WebDriverManager to set up the driver.
5. Create a Selenium browser instance.
6. Maximize the browser window.
7. Apply implicit wait.
8. Return the ready-to-use `WebDriver`.

Example browser selection:

```java
switch (browser) {
    case "firefox":
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        break;
    case "edge":
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        break;
    case "chrome":
    case "chromium":
    default:
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(chromeOptions());
        break;
}
```

Important syntax:

- `switch` chooses code based on a value.
- `case` is one possible value.
- `break` stops the switch from continuing.
- `default` runs if no previous case matches.

Chrome options are configured in:

```java
private static ChromeOptions chromeOptions()
```

If `headless=true`, this code runs:

```java
options.addArguments("--headless=new");
options.addArguments("--disable-gpu");
```

---

## 11. Base Test Flow: `BaseTest.java`

Path:

```text
src/test/java/com/automation/base/BaseTest.java
```

Every test class extends `BaseTest`:

```java
public class SearchAndSortTests extends BaseTest
```

That inheritance gives every test class:

- The shared `driver`.
- Automatic browser setup before every test method.
- Automatic browser teardown after every test method.
- Helper methods such as `loginAsConfiguredUser()` and `logoutIfPossible()`.

### `@BeforeMethod`

```java
@BeforeMethod
public void setUp() {
    driver = DriverFactory.createDriver();
    driver.get(ConfigReader.getHomeUrl());
}
```

TestNG runs this before every `@Test` method.

It creates a new browser session and opens the home page.

### `@AfterMethod`

```java
@AfterMethod(alwaysRun = true)
public void tearDown() {
    if (driver != null) {
        driver.quit();
    }
}
```

TestNG runs this after every `@Test` method.

`alwaysRun = true` means TestNG should still run teardown even if the test fails.

`driver.quit()` closes the whole browser session.

### Why a new browser per test?

Each test starts with a clean browser session. This reduces dependency between tests and makes failures easier to isolate.

---

## 12. Page Object Model

The project uses Page Object Model, usually shortened to POM.

The idea:

- Test classes describe the business scenario.
- Page classes know how to interact with specific screens or page areas.
- Locators stay inside page classes.
- Selenium details are kept away from test methods.

Example from a test:

```java
LoginPage loginPage = new LoginPage(driver);
loginPage.openMyAccountLogin();
loginPage.login(email, password);
```

The test says what is happening. The page object hides how Selenium finds and clicks the elements.

---

## 13. Base Page: `BasePage.java`

Path:

```text
src/main/java/com/automation/pages/BasePage.java
```

All page objects extend `BasePage`:

```java
public class LoginPage extends BasePage
```

This gives every page object:

- Access to `driver`.
- Access to explicit wait.
- Common click/type/text helper methods.
- Common navigation actions such as opening My Account login/register.
- Currency change logic.

Constructor:

```java
public BasePage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait", 15)));
}
```

Important syntax:

- `protected` means child classes can use the field.
- `final` means the field is assigned once and should not be reassigned.
- `this.driver` refers to the object field.
- The `driver` parameter is the value passed into the constructor.

### Locators

Selenium uses locators to find HTML elements.

Examples:

```java
private final By myAccountMenu = By.cssSelector("#top-links a[title='My Account']");
private final By registerLink = By.linkText("Register");
private final By loginLink = By.linkText("Login");
```

Meaning:

- `By.id("input-email")` finds an element by HTML `id`.
- `By.name("agree")` finds an element by HTML `name`.
- `By.linkText("Login")` finds a link with exact visible text.
- `By.cssSelector(...)` finds elements using CSS selector syntax.
- `By.xpath(...)` finds elements using XPath syntax.

### Common action helpers

Click:

```java
protected void click(By locator) {
    wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
}
```

Type:

```java
protected void type(By locator, String value) {
    WebElement element = waitVisible(locator);
    element.clear();
    element.sendKeys(value == null ? "" : value);
}
```

Read text:

```java
protected String text(By locator) {
    return waitVisible(locator).getText();
}
```

Why this matters:

- Tests are less flaky because the framework waits before interacting.
- Page classes avoid repeating the same Selenium code.
- If wait behavior changes, it can be changed in one place.

---

## 14. Page Classes

### `AccountPage.java`

Represents registration and account-related screens.

Main responsibilities:

- Fill first name, last name, email, telephone, password, and confirm password.
- Tick/untick the privacy policy agreement checkbox.
- Submit registration.
- Read success heading and validation messages.

Example:

```java
accountPage.enterFirstName(data.get("firstName"));
accountPage.enterLastName(data.get("lastName"));
accountPage.enterEmail(email);
accountPage.submitRegistration();
```

The field error method:

```java
public String getFieldErrorByInputId(String inputId) {
    return text(By.cssSelector("#" + inputId + " + .text-danger"));
}
```

This dynamically builds a CSS selector. For example:

```java
getFieldErrorByInputId("input-email")
```

becomes:

```css
#input-email + .text-danger
```

That selector means: find the `.text-danger` element immediately after the element with id `input-email`.

### `LoginPage.java`

Represents login behavior.

Main method:

```java
public void login(String email, String password) {
    type(emailInput, email);
    type(passwordInput, password);
    click(loginButton);
}
```

It also reads login warnings:

```java
public String getWarningMessage()
```

and checks if a warning exists:

```java
public boolean isWarningDisplayed()
```

### `CatalogPage.java`

Represents product category/search result pages.

Main responsibilities:

- Open categories by direct route.
- Sort products.
- Read product names.
- Select search category.
- Search with subcategories.
- Verify currency symbols.
- Add products to cart.
- Open product details.

Example:

```java
catalogPage.openPhonesAndPdas();
catalogPage.selectSortBy(data.get("sortAscending"));
List<String> names = catalogPage.getProductNames();
```

The `Select` class is Selenium's helper for HTML `<select>` dropdowns:

```java
new Select(waitVisible(sortDropdown)).selectByVisibleText(visibleText);
```

Dynamic XPath example:

```java
By.xpath("//div[contains(@class,'product-thumb')][.//h4/a[normalize-space()='" + productName
        + "']]//button[contains(@onclick,'" + onclickFragment + "')]")
```

This finds the product card that contains a product title matching `productName`, then finds the button whose `onclick` contains `cart.add`.

### `SearchPage.java`

Represents search inputs.

Main responsibilities:

- Search from the header.
- Open the advanced search page.
- Enter a keyword into the advanced search field.

### `ProductPage.java`

Represents an individual product details page.

Main responsibilities:

- Set delivery date.
- Add product to cart.
- Read success alert.

### `CartPage.java`

Represents the shopping cart.

Main responsibilities:

- Open cart.
- Go to checkout.
- Verify cart contains items.
- Verify product row contains expected details.
- Read grand total.
- Sum item line totals.
- Read the small cart text.

Money parsing:

```java
private BigDecimal parseMoney(String value) {
    String normalized = value.replaceAll("[^0-9.]", "");
    return normalized.isBlank() ? BigDecimal.ZERO : new BigDecimal(normalized);
}
```

This removes currency symbols and non-numeric characters so the value can be compared as a number.

`BigDecimal` is used instead of `double` because money comparisons should avoid floating-point rounding problems.

### `CheckoutPage.java`

Represents the checkout wizard.

Main responsibilities:

- Fill billing address.
- Continue shipping address.
- Enter delivery comments.
- Continue delivery method.
- Accept terms and conditions.
- Continue payment method.
- Confirm order.
- Read order success message.

It handles both guest checkout and logged-in checkout forms:

```java
if (!driver.findElements(guestCheckoutRadio).isEmpty()) {
    click(guestCheckoutRadio);
    click(continueAccountButton);
}
```

Important Selenium detail:

- `findElement(...)` fails if the element does not exist.
- `findElements(...)` returns an empty list if the element does not exist.

That is why this code uses `findElements(...).isEmpty()` for optional UI sections.

---

## 15. Test Data: Excel Workbook

Path:

```text
src/test/resources/testdata/testdata.xlsx
```

The workbook contains one sheet per test case:

| Sheet | Headers |
|---|---|
| `TC01` | `firstName`, `lastName`, `email`, `telephone`, `password`, `confirmPassword` |
| `TC02` | `firstName`, `lastName`, `email`, `telephone`, `shortPassword` |
| `TC03` | `email`, `password` |
| `TC04` | `email`, `password` |
| `TC05` | `email`, `password` |
| `TC06` | `email`, `password` |
| `TC07` | `sortAscending`, `sortDescending` |
| `TC08` | `searchTerm` |
| `TC09` | `searchTerm`, `category`, `expectedProduct` |
| `TC10` | `tabletProduct`, `laptopProduct`, `deliveryDate` |
| `TC11` | `product`, `firstName`, `lastName`, `address`, `city`, `postCode`, `deliveryComment` |

Each sheet uses:

- Row 1 as column headers.
- Row 2 onward as data rows.

If a sheet has multiple data rows, TestNG runs the connected test once for each row.

---

## 16. Excel Reader: `ExcelReader.java`

Path:

```text
src/main/java/com/automation/utils/ExcelReader.java
```

This class converts Excel rows into TestNG data provider data.

Main usage:

```java
return ExcelReader.readSheetAsMaps("TC01");
```

The default workbook path is:

```java
private static final String DEFAULT_WORKBOOK = "testdata/testdata.xlsx";
```

Because `testdata.xlsx` is under `src/test/resources`, Java can load it from the classpath.

### How Excel becomes Java data

Suppose sheet `TC08` has:

| searchTerm |
|---|
| iphone |

`ExcelReader` returns data shaped like this:

```java
Object[][] {
    { Map.of("searchTerm", "iphone") }
}
```

That is why test methods receive:

```java
public void tc08SearchByName(Map<String, String> data)
```

and read values like:

```java
data.get("searchTerm")
```

### Why `Object[][]`?

TestNG data providers must return either:

```java
Object[][]
```

or an iterator-compatible structure.

Each row in the outer array is one test execution. Each value inside the row is passed as a method argument.

In this project, every test method receives one argument:

```java
Map<String, String> data
```

So each data provider row contains one map.

---

## 17. TestNG Syntax Used in This Project

### `@Test`

Marks a method as a test:

```java
@Test(dataProvider = "tc01", priority = 1, description = "TC01 - Registration without errors")
public void tc01RegistrationWithoutErrors(Map<String, String> data) {
    ...
}
```

Meaning:

- `dataProvider = "tc01"` tells TestNG to get input data from the method named `tc01`.
- `priority = 1` gives this test an execution order inside the class.
- `description` appears in reports.

### `@DataProvider`

Marks a method that supplies test data:

```java
@DataProvider(name = "tc01")
public Object[][] tc01() throws IOException {
    return ExcelReader.readSheetAsMaps("TC01");
}
```

The `name` must match the `dataProvider` value used by `@Test`.

### `Assert`

Assertions verify expected behavior.

Examples:

```java
Assert.assertEquals(accountPage.getSuccessHeading(), "Your Account Has Been Created!");
Assert.assertTrue(accountPage.isLogoutVisible(), "Logout should be visible in the My Account menu");
Assert.assertFalse(results.isEmpty(), "Search results should be displayed");
```

If an assertion fails, the test fails.

The optional message helps explain the failure in reports.

### `throws IOException`

Data provider methods include:

```java
throws IOException
```

That means the method may fail while reading the Excel file. TestNG handles the exception and reports the data provider failure.

---

## 18. Test Class Flow

### `AuthenticationNavigationTests.java`

Contains TC01-TC06:

- Registration success.
- Registration validation errors.
- Valid login.
- Invalid login.
- Currency change.
- Breadcrumb and left menu navigation.

Important detail:

```java
private static String registeredEmail;
private static String registeredPassword;
```

TC01 creates a unique email and stores it. Later login tests can reuse the email/password from TC01.

Unique email creation:

```java
return base.substring(0, atIndex) + "+" + stamp + base.substring(atIndex);
```

If the Excel email is:

```text
student@test.com
```

the generated email becomes something like:

```text
student+1714590000000@test.com
```

This avoids duplicate registration failures.

### `SearchAndSortTests.java`

Contains TC07-TC09:

- Sort product names ascending and descending.
- Search by product name.
- Search with subcategories.

Before each scenario, it calls:

```java
loginAsConfiguredUser();
```

This helper comes from `BaseTest`.

The sorting checks compare neighboring product names:

```java
names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0
```

For ascending order, each item should be less than or equal to the next item.

### `CartAndCheckoutTests.java`

Contains TC10-TC11:

- Add products to the cart and verify totals.
- Complete checkout and verify success.

The cart total assertion:

```java
Assert.assertEquals(cartPage.getDisplayedGrandTotal(), cartPage.sumLineTotals());
```

This compares the displayed total with the calculated sum of cart line totals.

---

## 19. Full Execution Flow of One Test

Example: TC08 search by product name.

1. Maven runs `mvn test`.
2. Surefire reads `testng.xml`.
3. TestNG finds `SearchAndSortTests`.
4. TestNG sees:

   ```java
   @Test(dataProvider = "tc08")
   ```

5. TestNG calls:

   ```java
   tc08()
   ```

6. The data provider reads Excel sheet `TC08`.
7. TestNG starts one test execution for each Excel data row.
8. `BaseTest.setUp()` runs.
9. `DriverFactory.createDriver()` creates a browser.
10. Browser opens the configured home URL.
11. The test method runs:

    ```java
    loginAsConfiguredUser();
    SearchPage searchPage = new SearchPage(driver);
    searchPage.searchFromHeader(data.get("searchTerm"));
    ```

12. Page objects use Selenium to interact with the website.
13. Assertions verify the search result.
14. If the test fails, `AllureListener` attaches a screenshot.
15. `BaseTest.tearDown()` closes the browser.
16. TestNG records the result.
17. Allure receives the result data.

---

## 20. Allure Reporting and Screenshots

Allure result directory is configured in:

```text
src/test/resources/allure.properties
```

Current value:

```properties
allure.results.directory=target/allure-results
```

The listener:

```text
src/test/java/com/automation/listeners/AllureListener.java
```

listens for test failures:

```java
public void onTestFailure(ITestResult result)
```

If screenshots are enabled:

```java
if (!ConfigReader.getBoolean("screenshot.on.failure", true)) {
    return;
}
```

it calls:

```java
ScreenshotUtils.attachScreenshot(...)
```

`ScreenshotUtils` captures the browser screenshot as bytes and attaches it to Allure:

```java
byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
Allure.addAttachment(attachmentName, new ByteArrayInputStream(screenshot));
```

---

## 21. Important Java Syntax in This Project

### Package

Every Java file starts with a package:

```java
package com.automation.pages;
```

The package should match the folder structure:

```text
com/automation/pages
```

### Import

Imports allow the file to use classes from other packages:

```java
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
```

### Class

A class groups fields and methods:

```java
public class LoginPage extends BasePage {
    ...
}
```

### Inheritance

`extends` means one class inherits from another:

```java
public class LoginPage extends BasePage
```

`LoginPage` can use protected helper methods from `BasePage`, such as `type()` and `click()`.

### Constructor

A constructor creates an object:

```java
public LoginPage(WebDriver driver) {
    super(driver);
}
```

`super(driver)` calls the parent class constructor, which stores the driver and creates the wait object.

### Access modifiers

| Modifier | Meaning |
|---|---|
| `public` | Can be accessed from other classes. |
| `private` | Can only be accessed inside the same class. |
| `protected` | Can be accessed inside the same class and child classes. |
| `final` | Cannot be reassigned after initialization. |
| `static` | Belongs to the class, not an object instance. |

### Generic types

Example:

```java
Map<String, String>
```

This means a map where both keys and values are strings.

The Excel data uses maps like:

```text
"email" -> "test@example.com"
"password" -> "Pass1234"
```

### Lists

Example:

```java
List<String> results = catalogPage.getProductNames();
```

A `List<String>` is an ordered collection of strings.

### For loop

Example:

```java
for (String name : results) {
    Assert.assertTrue(name.toLowerCase().contains(data.get("searchTerm").toLowerCase()));
}
```

This loops through every product name in the results list.

### Conditional

Example:

```java
if (loginPage.isWarningDisplayed()) {
    registerNewUser();
}
```

This means: if login failed with a warning, register a new user.

### Ternary operator

Example:

```java
element.sendKeys(value == null ? "" : value);
```

Meaning:

- If `value == null`, type an empty string.
- Otherwise, type `value`.

---

## 22. Selenium Concepts Used

### `WebDriver`

`WebDriver` controls the browser:

```java
driver.get("https://example.com");
driver.quit();
driver.findElements(locator);
```

### `WebElement`

`WebElement` represents one HTML element:

```java
WebElement element = waitVisible(emailInput);
element.clear();
element.sendKeys(email);
```

### Explicit wait

Explicit wait waits for a specific condition:

```java
wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
```

This is used heavily in `BasePage`.

### Implicit wait

Implicit wait is configured once:

```java
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(...));
```

It tells Selenium to wait briefly when searching for elements.

### CSS selectors

Examples:

```java
By.cssSelector("#content h1")
By.cssSelector(".alert.alert-success")
By.cssSelector("input[type='submit'][value='Login']")
```

Quick CSS rules:

- `#id` selects an element by id.
- `.class` selects an element by class.
- `tag[attr='value']` selects a tag with a specific attribute.
- `parent child` selects descendants.

### XPath

Example:

```java
By.xpath("//p[contains(.,'There is no product that matches the search criteria.')]")
```

Quick XPath rules:

- `//p` means any `<p>` element anywhere.
- `contains(., 'text')` means the element text contains that text.
- `normalize-space()` trims extra spaces.

---

## 23. How to Add a New Test Case

Suppose you want to add `TC12`.

### Step 1: Add Excel sheet

Open:

```text
src/test/resources/testdata/testdata.xlsx
```

Create a new sheet:

```text
TC12
```

Add headers in row 1, for example:

```text
searchTerm | expectedProduct
```

Add data in row 2 onward.

### Step 2: Add a data provider

In the relevant test class:

```java
@DataProvider(name = "tc12")
public Object[][] tc12() throws IOException {
    return ExcelReader.readSheetAsMaps("TC12");
}
```

### Step 3: Add the test method

```java
@Test(dataProvider = "tc12", description = "TC12 - Example test")
public void tc12ExampleTest(Map<String, String> data) {
    SearchPage searchPage = new SearchPage(driver);
    searchPage.searchFromHeader(data.get("searchTerm"));

    CatalogPage catalogPage = new CatalogPage(driver);
    Assert.assertTrue(catalogPage.getProductNames().contains(data.get("expectedProduct")));
}
```

### Step 4: Add page methods if needed

If the test needs a new UI action, add it to the correct page class instead of writing Selenium directly in the test class.

Good:

```java
catalogPage.openTablets();
catalogPage.addProductToCart("Samsung Galaxy Tab 10.1");
```

Avoid:

```java
driver.findElement(By.xpath("...")).click();
```

inside the test method.

### Step 5: Update `testng.xml` if needed

If you created a new test class, add it:

```xml
<class name="com.automation.tests.NewTests"/>
```

If you only added a method to an existing listed class, no `testng.xml` change is needed.

---

## 24. Common Problems and Fixes

### Browser does not open

Check:

- Browser is installed.
- `browser` in `config.properties` is valid.
- Internet is available for WebDriverManager to download drivers if needed.

### Tests fail because element is not clickable

Possible causes:

- The page changed.
- The locator is no longer correct.
- A popup or alert is covering the element.
- The explicit wait is too short.

Check the relevant page class locator first.

### Excel data is not read

Check:

- Workbook exists at `src/test/resources/testdata/testdata.xlsx`.
- Sheet name exactly matches the Java call, for example `TC08`.
- Header names exactly match `data.get("...")`.
- The workbook is not open in a locked state by Excel.

### Login tests fail

The TutorialsNinja demo site may reset data or reject old accounts. The helper `loginAsConfiguredUser()` tries to register a new user if configured login fails.

For TC03, TC05, and TC06, the tests may use the email registered during TC01 because `AuthenticationNavigationTests` stores it in static variables.

### Allure report is empty

Run tests first:

```powershell
mvn test
```

Then generate or serve the report:

```powershell
mvn allure:report
```

or:

```powershell
mvn allure:serve
```

---

## 25. Framework Design Rules Used Here

This project follows these practical rules:

1. Test methods should describe scenarios, not low-level Selenium details.
2. Page classes should contain locators and UI actions.
3. Common Selenium actions should be centralized in `BasePage`.
4. Browser setup and teardown should be centralized in `BaseTest`.
5. Configuration should live outside Java code in `config.properties`.
6. Test input data should live outside Java code in Excel.
7. Reports and screenshots should be generated automatically.
8. Tests should start with a fresh browser session.

---

## 26. Quick Mental Model

Think of the framework like this:

```text
mvn test
  |
pom.xml
  |
Maven Surefire
  |
testng.xml
  |
TestNG test classes
  |
BaseTest creates browser
  |
DataProvider reads Excel
  |
Test method calls page objects
  |
Page objects use Selenium
  |
Assertions verify behavior
  |
Allure records result
  |
BaseTest closes browser
```

If you understand this chain, you understand the structure of the whole Part A framework.
