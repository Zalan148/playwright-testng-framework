package com.example.tests.smoke;

import com.example.framework.core.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EnvironmentSmokeTest extends BaseTest {
    @Test
    @Epic("Environment Switching")
    @Feature("Environment loads correctly")
    @Story("Load an environment")
    @Description("Verify able to load an environment")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyEnvironmentLoadsCorrectly() {
        Assert.assertNotNull(env.getBaseURL());
        Assert.assertNotNull(env.getApiBaseURL());
        Assert.assertNotNull(apiSecrets.getApiToken());
    }
}
