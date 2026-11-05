package com.example.framework.tests;

import com.example.framework.config.ConfigManager;
import com.example.framework.config.Secrets;
import com.example.framework.core.BaseTest;
import com.example.framework.pages.InventoryPage;
import com.example.framework.pages.LoginPage;
import com.example.framework.utils.VisualAssert;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Allure;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class VisualRegressionTest extends BaseTest {

    // Helper method to wait until page is fully stable
    private void waitForPageReady(Page page) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    private void tagTestCase(String testName) {
        String browserName = ConfigManager.getBrowser();

        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName(testName + " [" + browserName + "]")
        );
    }

    @Test
    public void loginPageVisualCheck() throws IOException {
        tagTestCase("loginPageVisualCheck");

        LoginPage loginPage = new LoginPage(page, env);
        loginPage.open();

        // Wait until login page is fully loaded
        waitForPageReady(page);

        // Mask dynamic elements (example: timestamp, rotating banner)
        Locator[] masks = {
                page.locator(".timestamp"),
                page.locator(".rotating-banner")
        };

        String browserName = ConfigManager.getBrowser();
        Path baselinePath = Paths.get("src/test/resources/baseline", browserName, "login.png");
        VisualAssert.assertScreenshot(page, baselinePath, 5, masks);

    }

    @Test
    public void inventoryPageVisualCheck() throws IOException {

        tagTestCase("InventoryPageVisualCheck");
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

        String browserName = ConfigManager.getBrowser();
        Path baselinePath = Paths.get("src/test/resources/baseline", browserName, "inventory.png");
        VisualAssert.assertScreenshot(page, baselinePath, 5, masks);

    }

}
