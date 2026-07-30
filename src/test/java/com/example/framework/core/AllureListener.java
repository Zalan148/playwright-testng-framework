package com.example.framework.core;

import org.testng.ITestListener;
import org.testng.ITestResult;


public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(">>> Listener triggered for: " + result.getName());
    }

}
