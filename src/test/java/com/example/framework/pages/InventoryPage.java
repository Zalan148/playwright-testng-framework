package com.example.framework.pages;

import com.example.framework.components.DropdownComponent;
import com.example.framework.components.ProductCard;
import com.example.framework.config.Environment;
import com.example.framework.core.BasePage;
import com.example.framework.core.Log;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private static final Logger log = Log.getLogger(InventoryPage.class);

    //locators
    private final Locator itemName = page.locator(".inventory_item_name");
    private final Locator cartBadge = page.locator(".shopping_cart_badge");
    private final Locator cartIcon = page.locator(".shopping_cart_link");

    private final Environment env;

    public InventoryPage(Page page, Environment env){
        super(page);
        this.env = env;
    }

    public void open(){
        page.navigate(env.getBaseURL() + "/inventory");
    }

    //Get all product names
    public List<String> getAllProductNames(){
        return itemName
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

    //component-based approach
    public List<ProductCard> getProducts() {
        return page.locator(".inventory_item")
                .all()
                .stream()
                .map(ProductCard::new)
                .toList();
    }

    //helper for cleaner tests
    public ProductCard getProductByName(String name) {
        return getProducts()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + name));
    }

    //Get cart count
    public int getCartCount() {
        if (cartBadge.count() == 0){
            return 0;
        }
        return Integer.parseInt(cartBadge.innerText());
    }

    //Navigate to cart
    @Step("Open cart")
    public void openCart(){
        log.info("Opening cart");
        cartIcon.click();
    }


    public DropdownComponent getSortDropdown() {
        Locator dropdownRoot = page.locator("select[data-test='product-sort-container']");
        return new DropdownComponent(dropdownRoot);
    }
}
