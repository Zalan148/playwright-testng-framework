/**
 * Network mocking allows tests to intercept, modify, or fully replace backend
 * API responses. This makes UI tests faster, more stable, and independent of
 * backend availability. Playwright provides several powerful patterns for
 * controlling network behaviour during test execution.
 *
 * 1. Full Mock (replace the entire response)
 *    - Useful when backend is unstable, slow, or when deterministic test data
 *      is required.
 *    - Example:
 *      page.route("../api/users", route -> {
 *          route.fulfill(new Route.FulfillOptions()
 *              .setStatus(200)
 *              .setContentType("application/json")
 *              .setBody("[{\"id\":1,\"name\":\"Alice\"}]"));
 *      });
 *
 * 2. Patch Real Responses (modify backend data)
 *    - Allows calling the real API, then adjusting the response before the UI
 *      receives it. Useful for simulating edge cases while keeping real API
 *      structure.
 *    - Example:
 *      page.route("../api/products", async route -> {
 *          Response response = await route.fetch();
 *          JsonArray json = await response.json();
 *          json.add(new JsonObject().put("name", "Injected Item"));
 *
 *          route.fulfill(new Route.FulfillOptions()
 *              .setResponse(response)
 *              .setJson(json));
 *      });
 *
 * 3. HAR Replay (record + replay network traffic)
 *    - Ideal for offline testing or when many endpoints must be mocked at once.
 *      HAR files capture real network traffic and allow deterministic replay.
 *    - Example:
 *      BrowserContext context = browser.newContext(
 *          new Browser.NewContextOptions().setRecordHarPath("network.har"));
 *      // Run the app once to record traffic
 *      // Later:
 *      BrowserContext replayContext = browser.newContext(
 *          new Browser.NewContextOptions().setHarPath("network.har"));
 *
 * 4. Blocking Requests (CSS, images, analytics)
 *    - Speeds up tests and removes unnecessary external calls.
 *    - Example:
 *      context.route("../*.png", route -> route.abort());
 *      context.route("../analytics/**", route -> route.abort());
 *
 * 5. Conditional Mocking (respond differently based on request details)
 *    - Useful for search, filtering, pagination, or simulating backend errors.
 *    - Example:
 *      page.route("../api/search", route -> {
 *          String url = route.request().url();
 *          if (url.contains("empty")) {
 *              route.fulfill(new Route.FulfillOptions().setBody("[]"));
 *          } else {
 *              route.fulfill(new Route.FulfillOptions()
 *                  .setBody("[{\"title\":\"Result for query\"}]"));
 *          }
 *      });
 *
 * Network mocking is typically used in enterprise applications where backend
 * services may be slow, unstable, or expensive to call. It enables fast,
 * reliable UI tests and allows simulation of error states that are difficult
 * to reproduce with real APIs. This class provides documentation only and does
 * not implement any specific mocking logic, keeping the framework clean while
 * offering guidance for future use.
 */


package com.example.framework.utils;

public class NetworkMocking {
    // Documentation-only class
}
