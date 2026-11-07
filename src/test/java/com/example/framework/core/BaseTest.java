package com.example.framework.core;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.ApiSecretsLoader;
import com.example.framework.config.ConfigManager;
import com.example.framework.config.Environment;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BaseTest {

    private static final Logger log = Log.getLogger(BaseTest.class);

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    public Page page;
    protected Environment env;
    protected ApiSecrets apiSecrets;
    protected String browserName;

    private List<String> consoleLogs = new ArrayList<>();

    @BeforeClass(alwaysRun = true)
    public void beforeClassSetup() {
        env = ConfigManager.getEnvironment();
        log.info("Running against environment: {}", env.getName());
        apiSecrets = ApiSecretsLoader.load(env.getName());
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(String browserParameter, Method method, ITestResult result) {
        consoleLogs.clear();
        Allure.parameter("browser", browserParameter);
        log.info("Starting test {} on thread {}",
              this.getClass().getSimpleName(),
              Thread.currentThread().getName());

        System.out.println("BASE DEBUG: Before Playwright.create()");
        playwright = Playwright.create();
        System.out.println("BASE DEBUG: After Playwright.create()");

        browserName = browserParameter.toLowerCase();

        BrowserType browserType = switch (browserParameter.toLowerCase()) {
          case "firefox" -> playwright.firefox();
          case "webkit" -> playwright.webkit();
          default -> playwright.chromium();
      };

        System.out.println("BASE DEBUG: Before browser.launch()");
        browser = browserType.launch(new BrowserType.LaunchOptions()
              .setHeadless(ConfigManager.isHeadless()));
        System.out.println("BASE DEBUG: After browser.launch()");
        context = browser.newContext(
              new Browser.NewContextOptions().setRecordVideoDir(Paths.get("videos")).setRecordVideoSize(1280,720)
      );
        page = context.newPage();
        System.out.println("BASE DEBUG: After page creation");
        page.setDefaultTimeout(ConfigManager.getDefaultTimeout());
        page.onConsoleMessage(msg -> {
            consoleLogs.add(msg.type() + ": " + msg.text());
      });

        // 1. Create a millisecond-precise time formatter
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        String currentTime = LocalTime.now().format(timeFormatter);


        // 2. Print the log with the timestamp at the very beginning
        System.out.println("[" + currentTime + "] SETUP: " + this.getClass().getSimpleName()
                + " on thread " + Thread.currentThread().getName());

        // 3. Print active groups for this test
        String[] groups = result.getMethod().getGroups();
        System.out.println("Active groups for test '" + method.getName() + "': "
                + (groups.length > 0 ? String.join(", ", groups) : "none"));

        // 4. Print browser info (after page is created)
        System.out.println("Playwright browser type: " + browserType.name());
        System.out.println("User agent: " + page.evaluate("navigator.userAgent"));

    }

  @AfterMethod
    public void tearDown(ITestResult result){

        String browserName = ConfigManager.getBrowser();

        log.info("Finished test {} on thread {}",
                this.getClass().getSimpleName(),
                Thread.currentThread().getName());

        Path videoPath = null;

    try {
        if (!result.isSuccess() && page != null) {

            //Capture screenshot
            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );

            io.qameta.allure.Allure.addAttachment(
                    "Screenshot (" + browserName + ")",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
            );

            //HTML Snapshot
            String html = page.content();
            io.qameta.allure.Allure.addAttachment(
                    "HTML Snapshot (" + browserName + ")",
                    "text/html",
                    new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)),
                    "html"
            );

            //Console logs
            if (!consoleLogs.isEmpty()) {
                String logs = String.join("\n", consoleLogs);
                io.qameta.allure.Allure.addAttachment(
                        "Console Logs (" + browserName + ")",
                        "text/plain",
                        new ByteArrayInputStream(logs.getBytes(StandardCharsets.UTF_8)),
                        "txt"
                );
            }

            // Save video path BEFORE closing anything
            videoPath = page.video().path();

        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    // PHASE 2 — Close page FIRST (this finalises the video)
    try{
        if (page != null) {
            page.close();  //finalises video
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

      //Then close the context
      context.close();

      // PHASE 3 — Now the video file is fully written
      try {
          if (!result.isSuccess() && videoPath != null) {

            byte[] videoBytes = Files.readAllBytes(videoPath);

            io.qameta.allure.Allure.addAttachment(
                    "Video (" + browserName + ")",
                  "video/mp4",
                  new ByteArrayInputStream(videoBytes),
                  "mp4"
          );
         }
      } catch (Exception e) {
          e.printStackTrace();
      }


      // PHASE 4 — CLEANUP CHUNK FILES (but keep the final merged video)
      try {
          if (videoPath != null) {
              File videosDir = new File("videos");
              File[] files = videosDir.listFiles();
              if (files != null) {
                  for (File file : files) {
                      //skip the final merged video
                      if (!file.toPath().equals(videoPath)) {
                          file.delete();
                      }
                  }
              }
          }
      } catch (Exception ignored) {}

      // PHASE 5 — Close browser + Playwright
      browser.close();
      playwright.close();
  }

}
