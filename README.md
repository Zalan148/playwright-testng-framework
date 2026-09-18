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
- Cleaner test data setup and teardown 


------------------------------------------------------------------


## Visual Regression Workflow

This framework uses Playwright + VisualAssert for screenshot-based regression testing.

Visual baselines are maintained separately for Chromium, Firefox and WebKit and are generated from the CI environment to provide consistent reference images.

### Workflow Checklist

#### Step 0 — Reset

Clear:

src/test/resources/baseline/
target/

Start from a clean slate when establishing or validating baselines.

#### Step 1 — Local baseline and visual regression run

Run the visual regression tests separately for each browser using the dedicated TestNG suite files:

mvn test -Dsurefire.suiteXmlFiles=testng-visual-chromium.xml -Dbrowser=chromium
mvn test -Dsurefire.suiteXmlFiles=testng-visual-firefox.xml -Dbrowser=firefox
mvn test -Dsurefire.suiteXmlFiles=testng-visual-webkit.xml -Dbrowser=webkit

Dedicated suite files provide the correct browser-specific hierarchy in Allure.

When baselines need to be generated or refreshed, run the tests explicitly with:

mvn test -Dtest=VisualRegressionTest -Dbaseline=true -Dbrowser=chromium

and repeat for Firefox and WebKit.

Baselines are stored separately:

src/test/resources/baseline/
├── chromium/
├── firefox/
└── webkit/

Normal visual regression runs compare screenshots against the existing baselines and do not overwrite them.

Note: Running the three browsers using the generic `-Dtest=VisualRegressionTest` approach works functionally, but the Allure hierarchy falls back to the Surefire suite/test structure. Use the dedicated browser-specific TestNG suite files when clean Allure labelling is required.

#### Step 2 — CI run with local baselines

Commit and push the current baselines:

git add src/test/resources/baseline/
git commit -m "Push local baselines"
git push origin main

CI runs visual-tests automatically for:

- Chromium
- Firefox
- WebKit

Expectation: tests fail if local baselines differ from CI rendering.

When failures occur, CI uploads:

- Current screenshots
- Diff screenshots
- Test videos
- Allure results containing failure attachments

Diff screenshots highlight visual differences.

#### Step 3 — Generate baselines in CI

Manually trigger baseline generation:

Actions → Run workflow → select `main`

CI generates baseline screenshots for each browser.

Expectation: baseline artifacts are produced separately for Chromium, Firefox and WebKit.

#### Step 4 — Commit CI baselines

Download the CI-generated baseline artifacts and copy the images into:

src/test/resources/baseline/<browser>/

Commit and push:

git add src/test/resources/baseline/
git commit -m "Commit CI-generated baselines"
git push origin main

CI runs visual-tests automatically.

Expectation: all browsers pass because CI is comparing against CI-generated baselines.

#### Step 5 — Regression confirmation

Any subsequent code change triggers visual-tests.

Expectation:

- Tests pass when the UI matches the approved baselines.
- Tests fail when an unexpected visual difference is detected.
- Diff screenshots highlight the differences.
- Current screenshots show the actual rendered page.
- Failure attachments and Allure results are uploaded.

### Baseline Refresh Policy

Baseline images are the "golden" reference for regression checks.

#### When to refresh

Refresh baselines only after intentional UI changes, such as:

- New layouts
- Design changes
- Approved visual updates

#### How to refresh

1. Trigger the CI baseline-generation workflow using `workflow_dispatch`.
2. Download the baseline artifacts for each browser.
3. Replace the corresponding images in:

   src/test/resources/baseline/<browser>/

4. Commit the updated baselines with a clear message, for example:

   git commit -m "Refresh CI baselines after UI update"
   git push origin main

#### Review

Baseline updates should be reviewed in pull requests to confirm that visual changes are deliberate and expected.

#### Golden Rule

**Never overwrite baselines simply to make a failing test pass.**

Investigate visual-regression failures first. Refresh the baseline only when the UI change is intentional and approved.