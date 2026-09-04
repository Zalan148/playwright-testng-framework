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

import com.example.framework.api.AuthClient;
import com.example.framework.core.BaseTest;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HybridLoginBypassTest extends BaseTest {
    private AuthClient authClient;

    @BeforeMethod(alwaysRun = true)
    public void setupClients() {
        authClient = new AuthClient(env, apiSecrets);
    }

    @Test
    public void loginViaApiAndBypassUiLogin() {

        // 1. API Login — Get token instantly
        APIResponse loginResponse = authClient.login(
                "admin_user",
                "admin_password123"
        );

        ResponseValidator.assertStatus(loginResponse, 200);

        String token = JsonUtils.getString(loginResponse, "token");

        // 2. Inject token into browser localStorage before page loads
        page.addInitScript("localStorage.setItem('authToken', '" + token + "')");

        // 3. Navigate directly to mock dashboard
        page.navigate("http://localhost:3001/index.html");

        // 4. UI Verification — Confirm dashboard loads
        assertThat(page.locator("div.inventory_list")).isVisible();

    }
}
