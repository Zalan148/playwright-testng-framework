package com.example.framework.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class VisualAssert {

    /**
     * Capture screenshot and compare against baseline.
     *
     * Flow:
     * - First run: If baseline does not exist → create it and exit.
     * - Baseline mode: If system property "baseline" is true → recreate baseline and exit.
     * - Otherwise: Capture current screenshot, compare with baseline, and generate diff.
     *
     * Supports masking of dynamic elements via locators.
     */
    public static void assertScreenshot(Page page, Path baselinePath, String browserName, int tolerance, Locator... masks) throws IOException {
        File baselineFile = baselinePath.toFile();

        // Ensure parent directories exist
        File parentDir = baselineFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("Created baseline directory: " + parentDir.getAbsolutePath());
            }
        }

        boolean baselineMode = Boolean.getBoolean("baseline");

        // Baseline creation logic
        Page.ScreenshotOptions baselineOptions = new Page.ScreenshotOptions().setPath(baselinePath);
        if (masks != null && masks.length > 0) {
            baselineOptions.setMask(Arrays.asList(masks));
        }
        if (baselineMode || !baselineFile.exists()) {
            page.screenshot(baselineOptions);
            System.out.println("Baseline created/updated: " + baselinePath);
            return;
        }

        // Current screenshot capture
        String baseName = baselinePath.getFileName().toString().replace(".png", "");

        // Add browser name into filenames to avoid overwriting
        Path currentPath = Paths.get("target", baseName + "-" + browserName + "-current.png");
        Path diffPath = Paths.get("target", baseName + "-" + browserName + "-diff.png");

        Page.ScreenshotOptions currentOptions = new Page.ScreenshotOptions().setPath(currentPath);
        if (masks != null && masks.length > 0) {
            currentOptions.setMask(Arrays.asList(masks));
        }
        page.screenshot(currentOptions);
        System.out.println("Current screenshot saved at: " + currentPath.toAbsolutePath());

        // Image comparison
        BufferedImage baselineImg = ImageIO.read(baselineFile);
        BufferedImage currentImg = ImageIO.read(currentPath.toFile());

        if (baselineImg == null || currentImg == null) {
            throw new AssertionError("Could not read baseline or current screenshot image.");
        }

        if (baselineImg.getWidth() != currentImg.getWidth() ||
                baselineImg.getHeight() != currentImg.getHeight()) {
            throw new AssertionError("Screenshot size mismatch!");
        }

        BufferedImage diffImg = new BufferedImage(
                baselineImg.getWidth(),
                baselineImg.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        boolean mismatchFound = false;

        for (int y = 0; y < baselineImg.getHeight(); y++) {
            for (int x = 0; x < baselineImg.getWidth(); x++) {
                int baselinePixel = baselineImg.getRGB(x, y);
                int currentPixel = currentImg.getRGB(x, y);

                if (pixelsAreEqual(baselinePixel, currentPixel, tolerance)) {
                    diffImg.setRGB(x, y, baselinePixel);
                } else {
                    diffImg.setRGB(x, y, Color.RED.getRGB());
                    mismatchFound = true;
                }
            }
        }


        ImageIO.write(diffImg, "png", diffPath.toFile());
        System.out.println("Diff screenshot saved at: " + diffPath.toAbsolutePath());

        if (mismatchFound) {
            System.out.println("Baseline mismatch detected. See " + currentPath + " and " + diffPath);
            throw new AssertionError("Visual regression detected! See " + diffPath + " for highlighted differences.");
        }
    }

    private static boolean pixelsAreEqual(int p1, int p2, int tolerance) {
        Color c1 = new Color(p1);
        Color c2 = new Color(p2);

        int diffR = Math.abs(c1.getRed() - c2.getRed());
        int diffG = Math.abs(c1.getGreen() - c2.getGreen());
        int diffB = Math.abs(c1.getBlue() - c2.getBlue());

        return (diffR <= tolerance && diffG <= tolerance && diffB <= tolerance);
    }
}
