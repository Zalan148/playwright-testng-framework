package com.example.tests.hybrid;

import com.example.framework.api.OrderClient;
import com.example.framework.core.BaseTest;
import com.example.framework.models.Order;
import com.example.framework.utils.JsonUtils;
import com.example.framework.utils.ResponseValidator;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ApiUiFlowTest extends BaseTest {
    private OrderClient orderClient;
    private String createdOrderId; // store the ID from API

    @BeforeClass(alwaysRun = true)
    public void setupApiClient() {
        orderClient = new OrderClient(env, apiSecrets);
    }

    @Test
    public void apiCreatesOrder() {
        long start = System.currentTimeMillis();

        // API call to create order
        APIResponse response = orderClient.createOrder("USER-001", "PROD-ABC", 2);

        long duration = System.currentTimeMillis() - start;

        // Validate status
        ResponseValidator.assertStatus(response, 201);

        // Deserialize payload
        Order createdOrder = JsonUtils.toPojo(response, Order.class);
        createdOrderId = createdOrder.getId(); // capture ID for UI check

        Allure.step("API created order ID: " + createdOrderId);
        Allure.addAttachment("Order JSON", "application/json", response.body().toString());

        // Performance budget: API response < 1500ms
        Assert.assertTrue(duration < 1500, "API response exceeded 1500ms");
    }

    @Test(dependsOnMethods = "apiCreatesOrder")
    public void uiVerifiesOrder() {
        long start = System.currentTimeMillis();

        // Login first
        page.navigate(env.getBaseURL());
        page.fill("#user-name", "standard_user");
        page.fill("#password", "secret_sauce");
        page.click("#login-button");

        // Add product to cart
        page.click("button[data-test='add-to-cart-sauce-labs-backpack']");

        // Go to cart and checkout
        page.click(".shopping_cart_link");
        page.click("#checkout");
        page.fill("#first-name", "Test");
        page.fill("#last-name", "User");
        page.fill("#postal-code", "12345");
        page.click("#continue");

        // Now we are on checkout-step-two.html
        Locator itemLocator = page.locator(".inventory_item_name");
        itemLocator.waitFor(new Locator.WaitForOptions().setTimeout(10000));

        String itemText = itemLocator.textContent();

        long duration = System.currentTimeMillis() - start;

        // Log steps in Allure
        Allure.step("UI item text: " + itemText);

        // Attach screenshot
        attachScreenshot(page);

        // JVM resource metrics
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        Allure.addAttachment("Memory Used (bytes)", String.valueOf(usedMemory));

        // Browser performance metrics
        Object perfTiming = page.evaluate("() => JSON.stringify(window.performance.timing)");
        Allure.addAttachment("Browser Performance Timing", perfTiming.toString());

        // Performance budgets
        Assert.assertTrue(duration < 2000, "UI render exceeded 2s");
        Assert.assertTrue(usedMemory < 500_000_000, "Memory usage exceeded 500MB");
        Assert.assertTrue(itemText.contains("Sauce Labs Backpack"), "Expected product not found in UI");
    }


    /**
     * Helper method to attach Playwright screenshot to Allure.
     */
    @Attachment(value = "UI Screenshot", type = "image/png")
    public byte[] attachScreenshot(Page page) {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }
}
