/**
 * Usage example:
 *
 * If a page contains a shadow DOM component, add a method like:
 *
 * public ShadowComponent getDatePicker() {
 *     return new ShadowComponent(page.locator("my-date-picker"));
 * }
 *
 * Then interact with it in tests:
 * ShadowComponent picker = page.getDatePicker();
 * picker.click("button.next-month");
 *
 * This class supports nested shadow roots using Playwright's piercing selectors.
 */

package com.example.framework.components;

import com.microsoft.playwright.Locator;

public class ShadowComponent {
    private final Locator root;

    public ShadowComponent(Locator root){
        this.root = root;
    }

    public Locator find(String selector) {
        return root.locator(">>" + selector);
    }

    public void click(String selector) {
        find(selector).click();
    }

    public String text(String selector) {
        return find(selector).innerText();
    }
}
