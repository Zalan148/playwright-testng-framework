package com.example.tests.smoke;

import com.example.framework.api.UserClient;
import com.example.framework.core.BaseTest;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import io.qameta.allure.*;
import org.testng.annotations.Test;

public class ApiSmokeTest extends BaseTest {
    @Test
    @Epic("API Layer")
    @Feature("User API")
    @Story("Get all users")
    @Description("Verify that the /users endpoint is reachable and returns 200")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyApiIsReachable() {

        UserClient userClient = new UserClient(env, apiSecrets);
        APIResponse response = userClient.getAllUsers();
        ResponseValidator.assertStatus(response, 200);
        Allure.addAttachment("Response Body", response.text());
    }
}
