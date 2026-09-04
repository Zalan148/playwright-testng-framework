package com.example.framework.tests;

import com.example.framework.core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TeardownValidationTest extends BaseTest {

    @Test(groups = "diagnostic", description = "Validates teardown captures all artifacts")
    @Feature("Diagnostic Validation")
    @Owner("QA Framework")
    @Description("This test intentionally fails after producing console logs, screenshots, HTML snapshot, and video. It validates that teardown consistently captures and attaches all artifacts in Allure.")
    public void teardownCapturesAllArtifacts(){

        //Navigate to a stable external page
        page.navigate("https://example.com");

        //Produce real browser console logs
        page.evaluate("console.log('LOG: teardown validation')");
        page.evaluate("console.warn('WARN: teardown validation')");
        page.evaluate("console.error('ERROR: teardown validation')");
        page.evaluate("console.info('INFO: teardown validation')");

        // Interact with a real element (should pass)
        page.locator("h1").isVisible();

        // Wait so video has meaningful content
        page.waitForTimeout(3000);

        // Trigger a failure to activate teardown
        Assert.fail("Intentional failure to validate teardown artifacts");
    }
}
