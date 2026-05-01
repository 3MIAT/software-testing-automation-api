package com.automation.core;

import com.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.getBrowser().trim().toLowerCase();
        WebDriver driver;

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

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait", 10)));
        return driver;
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        if (ConfigReader.getBoolean("headless", false)) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
        }

        String binary = ConfigReader.get("chrome.binary");
        if (binary != null && !binary.isBlank()) {
            options.setBinary(binary);
        }

        return options;
    }
}
