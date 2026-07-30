package com.example.framework.pages;

import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends BasePage {

    private static final Logger log = Log.getLogger(CartPage.class);

    private final String cartItem = ".cart_item";
    private final String itemName = ".inventory_item_name";
    private final String removeButton = "button[id^='remove']";
    private final String checkoutButton = "#checkout";

    public CartPage(Page page) {
        super(page);
    }

    //get all item names in the cart
    public List<String> getCartItemNames(){
        return page.locator(cartItem)
                .locator(itemName)
                .allInnerTexts()
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
    }


    //Remove a specific product from the cart
    @Step("remove item: {0}")
    public void removeItem(String productName){
        log.info("Removing item: {}", productName);

        page.locator(cartItem + ":has(" + itemName + ":has-text('" + productName + "'))")
                .locator(removeButton)
                .click();
    }

    //proceed to checkout
    @Step("Click checkout")
    public void clickCheckout() {
        log.info("Clicking checkout button");
        click(checkoutButton);
    }

}
