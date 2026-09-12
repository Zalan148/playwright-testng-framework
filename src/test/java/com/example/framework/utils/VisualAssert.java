package com.example.framework.utils;

import com.microsoft.playwright.Page;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class VisualAssert {

        /**
         * Capture screenshot and compare against baseline.
         *
         * Flow:
         * - First run: If baseline does not exist, create it and exit.
         *   → Only baseline images are created (no current/diff in target).
         * - Subsequent runs: Compare current screenshot against baseline.
         *   → Current and diff images are saved in target folder.
         *
         * Note: Pixel comparison uses tolerance to avoid false positives
         * from minor rendering differences (e.g., anti-aliasing).
         */
        public static void assertScreenshot(Page page, Path baselinePath, int tolerance) throws IOException {
            File baselineFile = baselinePath.toFile();

            // Ensure baseline folder exists
            baselineFile.getParentFile().mkdirs();

            // First run → create baseline and exit
            if (!baselineFile.exists()) {
                page.screenshot(new Page.ScreenshotOptions().setPath(baselinePath));
                System.out.println("Baseline created: " + baselinePath);
                return; // No current/diff images expected in target on initial run
            }

            // Derive names for current/diff based on baseline filename
            String baseName = baselinePath.getFileName().toString().replace(".png", "");
            Path currentPath = Paths.get("target", baseName + "-current.png");
            Path diffPath = Paths.get("target", baseName + "-diff.png");

            // Capture current screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(currentPath));
            System.out.println("Current screenshot saved at: " + currentPath.toAbsolutePath());

            // Load both images
            BufferedImage baselineImg = ImageIO.read(baselineFile);
            BufferedImage currentImg = ImageIO.read(currentPath.toFile());

            if (baselineImg == null || currentImg == null) {
                throw new AssertionError("Could not read baseline or current screenshot image.");
            }

            // Dimension check
            if (baselineImg.getWidth() != currentImg.getWidth() ||
                    baselineImg.getHeight() != currentImg.getHeight()) {
                throw new AssertionError("Screenshot size mismatch!");
            }

            // Generate diff image
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

            // Save diff image
            ImageIO.write(diffImg, "png", diffPath.toFile());
            System.out.println("Diff screenshot saved at: " + diffPath.toAbsolutePath());

            if (mismatchFound) {
                System.out.println("Baseline mismatch detected. See " + currentPath + " and " + diffPath);
                throw new AssertionError("Visual regression detected! See " + diffPath + " for highlighted differences.");
            }
        }

        // Helper method for tolerance-based pixel comparison
        private static boolean pixelsAreEqual(int p1, int p2, int tolerance) {
            Color c1 = new Color(p1);
            Color c2 = new Color(p2);

            int diffR = Math.abs(c1.getRed() - c2.getRed());
            int diffG = Math.abs(c1.getGreen() - c2.getGreen());
            int diffB = Math.abs(c1.getBlue() - c2.getBlue());

            return (diffR <= tolerance && diffG <= tolerance && diffB <= tolerance);
        }
}
