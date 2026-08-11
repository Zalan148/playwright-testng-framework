/**
 * Usage example:
 *
 * Multi‑tab workflows occur when an action opens a new browser tab or window.
 *
 * This component wraps Playwright's native multi‑tab APIs:
 *
 * Raw Playwright (Pattern 1):
 * Page newTab = page.waitForPopup(() -> {
 *     page.click("#open-new-tab-button");
 * });
 *
 * Raw Playwright (Pattern 2):
 * Page newTab = page.context().pages().get(1);
 *
 * Using this component:
 *
 * Page newTab = tabs.waitForNewTab(() -> {
 *     page.locator("#open-new-tab").click();
 * });
 *
 * Page secondTab = tabs.getTab(1); // index 0 = original tab
 *
 * This is useful for OAuth login, payment redirects,
 * external verification pages, and embedded admin tools.
 */


package com.example.framework.components;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

public class MultiTabComponent {

    private final BrowserContext context;

    public MultiTabComponent(BrowserContext context){
        this.context = context;
    }

    public Page waitForNewTab(Runnable action){
        return context.waitForPage(action);
    }

    public Page getTab(int index){
        return context.pages().get(index);
    }
}
