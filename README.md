# Playwright TestNG Hybrid Suite

## Overview
This project demonstrates **hybrid testing** — combining API calls with UI verification — using Playwright + TestNG.

SauceDemo was originally used for UI tests, but it does not support:
- Token injection into localStorage
- API-driven flows (orders, products, users)

To overcome this, we created a **mock environment**:
- **Mock API**: defined in `db.json` and served by `server.js`
- **Mock Frontend**: static pages in `/public` (`index.html`, `orders.html`, `products.html`, `login.html`)
- **Hybrid Tests**: Playwright injects data into localStorage, navigates to mock pages, and verifies UI elements

## Key Classes
- `BaseTest`: Common Playwright + TestNG setup
- `AuthClient`, `OrderClient`, `ProductClient`, `UserClient`: API clients for hybrid flows
- `ResponseValidator`, `JsonUtils`: Utilities for API response validation and parsing
- Hybrid test classes:
    - `HybridLoginBypassTest`
    - `HybridOrderFlowTest`
    - `HybridProductFlowTest`
    - `HybridUserFlowTest`

## Running the Hybrid Suite
1. **Start the mock server** (serves both API + frontend):
   ```bash
   npm run mock-api
This runs server.js and serves:

  - API endpoints at http://localhost:3001

  - Frontend pages at http://localhost:3001/public

2. Run the TestNG suite:

  - From IntelliJ: right‑click testng.xml → Run

  - From Maven CLI: mvn test -DsuiteXmlFile=testng.xml

3. Flow:
  - API creates data (user, order, product, token)

  - Playwright injects into localStorage

  - UI navigates to mock frontend page

  - Assertions confirm data is visible

Notes
  - These tests are conceptual examples of hybrid testing.

  - Once the framework is complete and wired to a real frontend consuming the mock API, they can be adapted to run against that environment.

  - For pure UI tests, continue using SauceDemo. For hybrid flows, use the mock setup.

## Hybrid Testing Patterns (Reference)

**API + UI Hybrid Testing** combines backend API calls with UI interactions to create fast, stable, and highly effective end‑to‑end tests. Instead of relying solely on the UI to prepare or verify state, tests use API calls for setup, teardown, and backend validation, while the UI is used only for the parts that need visual confirmation.

### Common hybrid testing patterns

1. **API Setup + UI Verification**
    - Create test data via API (users, orders, carts).
    - Navigate to the UI and verify the data is displayed correctly.
    - Example:
      ```java
      api.createUser("testUser");
      page.navigate("/users");
      page.locator("text=testUser").isVisible();
    ```

2. **API Login + UI Navigation (token injection)**
    - Authenticate via API and inject the token into browser storage.
    - Skip the UI login flow entirely.
    - Example:
      ```java
      String token = api.login("user", "pass");
      page.addInitScript("localStorage.setItem('authToken', '" + token + "')");
      page.navigate("/dashboard");
    ```

3. **UI Action + API Verification**
    - Perform an action in the UI (submit form, create order).
    - Verify the backend state using API calls.
    - Example:
      ```java
      page.locator("#submit").click();
      api.verifyOrderExists(orderId);
    ```

4. **API Cleanup (teardown)**
    - Remove test data via API instead of using slow UI flows.
    - Example:
      ```java
      api.deleteUser("testUser");
    ```

5. **Complex State Preparation**
    - Create scenarios that are difficult or impossible to produce via UI.
    - Example:
      ```java
      api.createOrderWithStatus("FAILED_PAYMENT");
      page.navigate("/orders");
    ```

### Benefits of hybrid testing
- Faster test execution
- More stable tests with fewer UI dependencies
- Ability to test edge cases and backend‑driven scenarios
- Cleaner test data setup and teardown  # trigger regression
