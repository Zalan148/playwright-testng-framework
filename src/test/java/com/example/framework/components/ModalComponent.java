
/**
 * Usage example:
 *
 * If a page contains a modal, add a method like:
 *
 * public ModalComponent getModal() {
 *     return new ModalComponent(page.locator("#modal-root"));
 * }
 *
 * Then interact with it in tests:
 * ModalComponent modal = page.getModal();
 * modal.confirm();
 */


package com.example.framework.components;

import com.microsoft.playwright.Locator;

public class ModalComponent {
    private final Locator root;

    public ModalComponent(Locator root){
        this.root = root;
    }

    public String getTitle() {
        return root.locator(".modal-title, .header").innerText();
    }

    public String getBodyText() {
        return root.locator(".modal-body, .content").innerText();
    }

    public void confirm() {
        root.locator("button:has-text('OK'), button:has-text('Confirm'), [data-test='confirm']").click();
    }

    public void cancel() {
        root.locator("button:has-text('Cancel'), [data-test='cancel']").click();
    }

    public void close() {
        root.locator(".close, .modal-close, [data-test='close']").click();
    }

    public boolean isVisible() {
        return root.isVisible();
    }
}
