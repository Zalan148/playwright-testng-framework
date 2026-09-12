package com.example.framework.tests;

import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import com.example.framework.utils.VisualAssert;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;


public class VisualRegressionTest extends BaseTest {

    // Helper method to wait until page is fully stable
    private void waitForPageReady(Page page) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Test
    public void loginPageVisualCheck() throws IOException {
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();

        // Wait until login page is fully loaded
        waitForPageReady(page);

        // Mask dynamic elements (example: timestamp, rotating banner)
        Locator[] masks = {
                page.locator(".timestamp"),
                page.locator(".rotating-banner")
        };

        VisualAssert.assertScreenshot(page, Paths.get("src/test/resources/baseline/login.png"), 5, masks);

    }

    @Test
    public void inventoryPageVisualCheck() throws IOException {
        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();
        InventoryPage inventoryPage = loginPage.login(Secrets.getUsername(), Secrets.getPassword());

        // Wait until login page is fully loaded
        waitForPageReady(page);

        // Mask dynamic elements (example: timestamp, rotating banner)
        Locator[] masks = {
                page.locator(".timestamp"),
                page.locator(".rotating-banner")
        };

        VisualAssert.assertScreenshot(page, Paths.get("src/test/resources/baseline/inventory.png"), 5, masks);

    }

}
