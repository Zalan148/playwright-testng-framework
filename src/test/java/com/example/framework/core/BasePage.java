package com.example.framework.core;

import com.microsoft.playwright.Page;

public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    //click element
    protected void click(String locator){
        page.locator(locator).click();
    }

    //Type text
    protected void type(String locator, String text) {
        page.locator(locator).fill(text);
    }

    //Get text
    protected String getText(String locator) {
        return page.locator(locator).innerText();
    }

    //wait for element to be visible
    protected void waitForVisibility(String locator){
        page.locator(locator).waitFor();
    }

    //navigate to URL
    public void navigate(String url) {
        page.navigate(url);
    }
}
