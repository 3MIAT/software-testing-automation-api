package com.automation.listeners;

import com.automation.base.BaseTest;
import com.automation.config.ConfigReader;
import com.automation.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        if (!ConfigReader.getBoolean("screenshot.on.failure", true)) {
            return;
        }
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            ScreenshotUtils.attachScreenshot(((BaseTest) testInstance).getDriver(), "Failure - " + result.getName());
        }
    }
}
