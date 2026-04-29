package com.automation.base;

import com.automation.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getBrowser().toLowerCase();

        if (browser.equals("chrome") || browser.equals("chromium")) {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            // Point to Chromium binary on Arch Linux
            options.setBinary("/usr/bin/chromium");

            // Recommended flags for stability on Linux
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            // Uncomment below to run headless (no visible browser window):
            // options.addArguments("--headless=new");

            driver = new ChromeDriver(options);
        } else {
            throw new RuntimeException("❌ Unsupported browser in config.properties: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        driver.manage().window().maximize();
        driver.get(ConfigReader.getBaseUrl());
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Exposes driver to AllureListener for screenshot capture
     */
    public WebDriver getDriver() {
        return driver;
    }
}
