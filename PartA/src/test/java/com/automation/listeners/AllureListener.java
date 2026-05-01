package com.automation.listeners;

import com.automation.base.BaseTest;
import com.automation.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testInstance = result.getInstance();
        if (testInstance instanceof BaseTest) {
            ScreenshotUtils.takeScreenshot(
                    ((BaseTest) testInstance).getDriver(),
                    "Failure — " + result.getName()
            );
        }
    }

    @Override
    public void onTestStart(ITestResult result) {}

    @Override
    public void onTestSuccess(ITestResult result) {}

    @Override
    public void onTestSkipped(ITestResult result) {}
}
