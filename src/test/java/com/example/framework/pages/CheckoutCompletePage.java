package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public class CheckoutCompletePage extends BasePage {


    private final Locator completeHeader = page.locator(".complete-header");

    public CheckoutCompletePage(Page page){
        super(page);
    }

    public String getCompleteMessage() {
        return completeHeader.innerText().trim();
    }
}
