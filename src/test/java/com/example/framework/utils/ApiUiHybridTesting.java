/**
 * API + UI Hybrid Testing combines backend API calls with UI interactions
 * to create fast, stable, and highly effective end‑to‑end tests. Instead of
 * relying solely on the UI to prepare or verify state, tests use API calls
 * for setup, teardown, and backend validation, while the UI is used only
 * for the parts that need visual confirmation.
 *
 * Common hybrid testing patterns:
 *
 * 1. API Setup + UI Verification
 *    - Create test data via API (users, orders, carts).
 *    - Navigate to the UI and verify the data is displayed correctly.
 *    - Example:
 *      api.createUser("testUser");
 *      page.navigate("/users");
 *      page.locator("text=testUser").isVisible();
 *
 * 2. API Login + UI Navigation (token injection)
 *    - Authenticate via API and inject the token into browser storage.
 *    - Skip the UI login flow entirely.
 *    - Example:
 *      String token = api.login("user", "pass");
 *      page.addInitScript("localStorage.setItem('authToken', '" + token + "')");
 *      page.navigate("/dashboard");
 *
 * 3. UI Action + API Verification
 *    - Perform an action in the UI (submit form, create order).
 *    - Verify the backend state using API calls.
 *    - Example:
 *      page.locator("#submit").click();
 *      api.verifyOrderExists(orderId);
 *
 * 4. API Cleanup (teardown)
 *    - Remove test data via API instead of using slow UI flows.
 *    - Example:
 *      api.deleteUser("testUser");
 *
 * 5. Complex State Preparation
 *    - Create scenarios that are difficult or impossible to produce via UI.
 *    - Example:
 *      api.createOrderWithStatus("FAILED_PAYMENT");
 *      page.navigate("/orders");
 *
 * Benefits of hybrid testing:
 *    - Faster test execution.
 *    - More stable tests with fewer UI dependencies.
 *    - Ability to test edge cases and backend-driven scenarios.
 *    - Cleaner test data setup and teardown.
 *
 * This class provides documentation only and does not implement any API
 * client or UI logic. It serves as a reference for how API + UI hybrid
 * testing should be applied in real-world enterprise automation.
 */

package com.example.framework.utils;

public class ApiUiHybridTesting {
    // Documentation-only class
}
