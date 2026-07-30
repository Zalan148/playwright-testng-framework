package com.example.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static Properties properties;

    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    public static synchronized Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                properties.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config file", e);
            }
        }
        return properties;
    }

    public static synchronized String get(String key) {
        String override = System.getProperty(key);
        if (override != null) return override;
        return getProperties().getProperty(key);
    }

    public static String getBaseUrl() {
        String env = get("env").toLowerCase();
        return get("baseUrl." + env);
    }

    public static String getSecret(String key){
        return System.getenv(key);
    }

    public static String getBrowser() {
       String browser = get("browser");
       log.info("Browser: {}", browser);
       return browser;

    }

    public static boolean isHeadless() {
       boolean headless = Boolean.parseBoolean(get("headless"));
       log.info("Headless: {}", headless);
       return headless;
    }

    public static int getDefaultTimeout() {
        int defaultTimeout = Integer.parseInt(get("defaultTimeout"));
        log.info("Timeout: {}", defaultTimeout);
        return defaultTimeout;
    }

    public static int getRetryCount(){
        return Integer.parseInt(get("retryCount"));
    }

    public static String getApiBaseUrl() {
        return get("api.baseUrl");
    }

    public static int getApiTimeout() {
        return Integer.parseInt(get("api.timeout"));
    }



}
