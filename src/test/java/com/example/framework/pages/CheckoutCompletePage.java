package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.microsoft.playwright.Page;


public class CheckoutCompletePage extends BasePage {

    private final String completeHeader = ".complete-header";

    public CheckoutCompletePage(Page page){
        super(page);
    }

    public String getCompleteMessage() {
        return page.locator(completeHeader).innerText().trim();
    }
}
