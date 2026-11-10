/**
 * Authentication flows in modern applications can involve multiple patterns:
 *
 * 1. Basic UI Login
 *    - Standard username + password form.
 *    - Example:
 *      page.locator("#username").fill("user");
 *      page.locator("#password").fill("pass");
 *      page.locator("#login").click();
 *
 * 2. OAuth Login (multi‑tab workflow)
 *    - Clicking "Login with Google/Microsoft" opens a new browser tab.
 *    - Example using MultiTabComponent:
 *      Page oauthTab = tabs.waitForNewTab(() -> {
 *          page.locator("#login-with-google").click();
 *      });
 *      oauthTab.locator("#identifierId").fill("user@gmail.com");
 *
 * 3. SSO Login (redirect workflow)
 *    - The same tab navigates to an external identity provider (Okta, Auth0, Azure AD).
 *    - Example:
 *      page.locator("#login").click();
 *      page.waitForURL("https://login.microsoftonline.com/**");
 *      page.locator("#username").fill("user@company.com");
 *
 * 4. MFA (multi‑step verification)
 *    - SMS, email, authenticator app, or push notification.
 *    - Example:
 *      page.locator("#code").fill("123456");
 *      page.locator("#verify").click();
 *
 * 5. Token‑based Login (bypass UI login)
 *    - Call the login API, extract the token, inject it into browser storage.
 *    - Example:
 *      String token = api.login("user", "pass");
 *      page.addInitScript("localStorage.setItem('authToken', '" + token + "')");
 *      page.navigate("https://app/dashboard");
 *
 * 6. Session Reuse (Playwright storage state)
 *    - Save authenticated session once, reuse it across tests.
 *    - Example:
 *      browserContext.storageState(new StorageStateOptions()
 *          .setPath(Paths.get("auth.json")));
 *
 *      browser.newContext(new Browser.NewContextOptions()
 *          .setStorageStatePath(Paths.get("auth.json")));
 *
 * This component does not implement any specific authentication flow,
 * but documents how to handle them in real applications using Playwright.
 */


package com.example.framework.components;

public class AuthComponent {
    // Documentation-only class
}
