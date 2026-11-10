package com.example.framework.pages;

import com.example.framework.components.CartItem;
import com.example.framework.config.Environment;
import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final Logger log = Log.getLogger(CartPage.class);

    private final Locator cartItems = page.locator(".cart_item");
    private final Locator itemNames = page.locator(".inventory_item_name");
    private final Locator removeButtons = page.locator("button[id^='remove']");
    private final Locator checkoutButton = page.locator("#checkout");

    private final Environment env;

    public CartPage(Page page, Environment env) {
        super(page);
        this.env = env;
    }

    public void open(){
        page.navigate(env.getBaseURL() + "/cart");
    }

    //get all item names in the cart
    public List<String> getCartItemNames(){
        return itemNames.allInnerTexts()
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }

    //Remove a specific product from the cart
    @Step("remove item: {0}")
    public void removeItem(String productName){
        log.info("Removing item: {}", productName);

        page.locator( ".cart_item:has(.inventory_item_name:has-text('" + productName + "'))")
                .locator("button[id^='remove']")
                .click();
    }

    public List<CartItem> getCartItems(){
        return page.locator(".cart_item")
                .all()
                .stream()
                .map(CartItem::new)
                .toList();
    }

    public CartItem getCartItemByName(String name) {
        return getCartItems()
                .stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + name));
    }

    //proceed to checkout
    @Step("Click checkout")
    public void clickCheckout() {
        log.info("Clicking checkout button");
        checkoutButton.click();
    }
}
