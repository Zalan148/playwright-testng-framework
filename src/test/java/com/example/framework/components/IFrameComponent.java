/**
 * Usage example:
 *
 * If a page contains an iframe, add a method like:
 *
 * public IFrameComponent getPaymentFrame() {
 *     return new IFrameComponent(page.frameLocator("#payment-frame"));
 * }
 *
 * Then interact with it in tests:
 *
 * IFrameComponent frame = checkoutPage.getPaymentFrame();
 * frame.type("#card-number", "4242 4242 4242 4242");
 * frame.click("#submit");
 *
 * This class supports nested iframes using Playwright's frameLocator API.
 */

package com.example.framework.components;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;

public class IFrameComponent {

    private final FrameLocator frame;

    public IFrameComponent(FrameLocator frame){
        this.frame = frame;
    }

    public Locator find(String selector){
        return frame.locator(selector);
    }

    public void click(String selector) {
        find(selector).click();
    }

    public void type(String selector, String text){
        find(selector).fill(text);
    }

    public String text(String selector){
        return find(selector).innerText();
    }
}
