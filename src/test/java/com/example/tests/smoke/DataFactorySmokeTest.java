package com.example.tests.smoke;

import com.example.framework.api.UserClient;
import com.example.framework.core.BaseTest;
import com.example.framework.models.User;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import io.qameta.allure.*;
import org.testng.annotations.Test;

public class DataFactorySmokeTest extends BaseTest {
    @Test
    @Epic("Data Management")
    @Feature("User creation")
    @Story("Create a user")
    @Description("Verify able to create a user dynamically and return 201")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyDynamicUserCreation() {
        UserClient userClient = new UserClient(env, apiSecrets);

        // Create a dynamic user
        APIResponse response = userClient.createUser("smoke_user", "smoke_user@example.com");
        ResponseValidator.assertStatus(response, 201);

        // Parse into POJO
        User user = JsonUtils.toPojo(response, User.class);
        assert user.getId() != null;

        // Cleanup
        userClient.deleteUser(user.getId());
    }
}
