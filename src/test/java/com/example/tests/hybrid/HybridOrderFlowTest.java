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

import com.example.framework.api.OrderClient;
import com.example.framework.core.BaseTest;
import com.example.framework.models.Order;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HybridOrderFlowTest extends BaseTest {

    private OrderClient orderClient;

    @BeforeMethod(alwaysRun = true)
    public void setupClients() {
        orderClient = new OrderClient(env, apiSecrets);
    }

    @Test
    public void createOrderViaApiAndVerifyInUi() {
        // 1. API Setup — Create order instantly
        APIResponse createResponse = orderClient.createOrder(
                "USER-001",
                "PROD-ABC",
                2
        );

        ResponseValidator.assertStatus(createResponse, 201);

        System.out.println(createResponse.text());
        Order createdOrder = JsonUtils.toPojo(createResponse, Order.class);

        // Save order ID into localStorage so orders.html can read it
        page.addInitScript("localStorage.setItem('lastOrderId', '" + createdOrder.getId() + "')");

        // Navigate to mock orders page served by your Node server
        page.navigate("http://localhost:3001/orders.html");

        // Assert order ID is visible
        assertThat(page.locator("text=" + createdOrder.getId())).isVisible();

        // API Cleanup — Remove order
        orderClient.deleteOrder(createdOrder.getId());
    }
}
