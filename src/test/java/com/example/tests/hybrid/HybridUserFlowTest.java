/**
 * Hybrid Test Suite Notice
 *
 * These hybrid tests (HybridLoginBypassTest, HybridOrderFlowTest, HybridProductFlowTest, HybridUserFlowTest)
 * are designed to demonstrate API + UI combined flows using a mock environment.
 *
 * Why not SauceDemo?
 * - SauceDemo does not support token injection or API-driven flows.
 * - Its frontend is tightly coupled to its own backend, so bypassing login or injecting orders/products
 *   via API is not possible.
 *
 * What we use instead:
 * - A mock API defined in db.json and served by server.js.
 * - A mock frontend created in the /public folder (index.html, orders.html, products.html, login.html).
 * - Playwright tests inject data into localStorage and navigate to these mock pages to verify UI behavior.
 *
 * Key classes:
 * - BaseTest: common Playwright + TestNG setup.
 * - AuthClient, OrderClient, ProductClient, UserClient: API clients for hybrid flows.
 * - ResponseValidator, JsonUtils: utilities for API response validation and parsing.
 *
 * How to run:
 * 1. Start the combined mock server:
 *    npm run mock-api
 *    (this serves both API endpoints and static frontend on http://localhost:3001)
 *
 * 2. Run the TestNG suite from IntelliJ or via Maven:
 *    mvn test -DsuiteXmlFile=testng.xml
 *
 * 3. Tests will call the mock API, inject data into localStorage, and verify UI elements
 *    on the mock frontend pages.
 *
 * Note:
 * These tests are conceptual examples of hybrid testing. Once the framework is complete and wired
 * to a real frontend consuming the mock API, they can be adapted to run against that environment.
 */


package com.example.tests.hybrid;

import com.example.framework.api.UserClient;
import com.example.framework.core.BaseTest;
import com.example.framework.models.User;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HybridUserFlowTest extends BaseTest {
    private UserClient userClient;

    @BeforeMethod(alwaysRun = true)
    public void setupClients() {
        userClient = new UserClient(env, apiSecrets);
    }

    @Test
    public void createUserViaApiAndLoginViaUi() {
        // 1. API Setup — Create user instantly
        APIResponse createResponse = userClient.createUser(
                "zalan_test_user",
                "zalan_test@example.com"
        );

        ResponseValidator.assertStatus(createResponse, 201);

        User createdUser = JsonUtils.toPojo(createResponse, User.class);

        // 2. Inject credentials into localStorage
        page.addInitScript("localStorage.setItem('username', '" + createdUser.getUsername() + "')");
        page.addInitScript("localStorage.setItem('password', 'defaultPassword123')");

        // 3. UI Login — Navigate to mock login page
        page.navigate("http://localhost:3001/login.html");

        page.locator("#username").fill(createdUser.getUsername());
        page.locator("#password").fill("defaultPassword123");
        page.locator("#loginBtn").click();

        // 4. UI Verification — Confirm dashboard loads
        assertThat(page.locator("text=Dashboard")).isVisible();

        // 5. API Cleanup — Remove user
        userClient.deleteUser(createdUser.getId());
    }
}
