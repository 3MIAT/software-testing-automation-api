package com.automation.utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {
    private ScreenshotUtils() {
    }

    public static void attachScreenshot(WebDriver driver, String attachmentName) {
        if (driver == null) {
            return;
        }
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(attachmentName, new ByteArrayInputStream(screenshot));
    }
}
