package com.example.framework.components;

import com.microsoft.playwright.Locator;

public class ProductCard {
    private final Locator root;

    public ProductCard(Locator root) {
        this.root = root;
    }

    public String getName() {
        return root.locator(".inventory_item_name").innerText();
    }

    public String getDescription(){
        return root.locator(".inventory_item_desc").innerText();
    }

    public String getPrice(){
        return root.locator(".inventory_item_price").innerText();
    }

    public void addToCart() {
        root.locator("button").click();
    }

    public void openDetails() {
        root.locator(".inventory_item_img").click();
    }
}
