package com.example.framework.tests;

import com.example.framework.core.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScreenshotTest extends BaseTest {

    @Test
    public void longVideoTest() {
        page.navigate("https://example.com");
        page.waitForTimeout(5000);  //5 secs
        page.locator("#this-element-does-not-exist").click(); //fails
    }

    //This method can be deleted. It was added to prove listener works, allure attaches logs, txt file appears and teardown logic is correct
    @Test
    public void consoleLogTest(){
        //navigate to any page
        page.navigate("https://example.com");

        //produce real browser console logs
        page.evaluate("console.log('LOG: This is a log message')");
        page.evaluate("console.warn('WARN: This is a warning message')");
        page.evaluate("console.error('ERROR: This is an error message')");
        page.evaluate("console.info('INFO: This is an info message')");

        //Force a failure so teardown runs
        Assert.fail("Triggering failure to capture console logs");
    }


    @Test
    public void passingTest() {
        page.navigate("https://example.com");
        page.locator("h1").isVisible(); // passes
    }



}
