package test.tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import test.config.Config;
import test.core.DriverFactory;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        String browser = Config.get("browser");
        int implicitWait = Config.getInt("implicitWait", 5);
        driver = DriverFactory.createDriver(browser, implicitWait);
        driver.get(Config.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
