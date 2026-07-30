package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private static final Logger log = Log.getLogger(InventoryPage.class);

    //locators
    private final String itemName = ".inventory_item_name";
    private final String cartBadge = ".shopping_cart_badge";
    private final String cartIcon = ".shopping_cart_link";

    public InventoryPage(Page page){
        super(page);
    }

    //Get all product names
    public List<String> getAllProductNames(){
        return page.locator(itemName)
                .allInnerTexts()
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    //Add a specific product to cart by name
    @Step("Add product to cart: {0}")
    public void addProductToCart(String productName) {
        log.info("Adding product to cart: {}", productName);
        page.locator(".inventory_item:has(.inventory_item_name:has-text('" + productName + "'))")
                .locator("button[id^='add-to-cart']")
                .click();
    }


    //Get cart count
    public int getCartCount() {
        if (page.locator(cartBadge).count() == 0){
            return 0;
        }
        return Integer.parseInt(page.locator(cartBadge).innerText());
    }

    //Navigate to cart
    @Step("Open cart")
    public void openCart(){
        log.info("Opening cart");
        click(cartIcon);
    }
}
