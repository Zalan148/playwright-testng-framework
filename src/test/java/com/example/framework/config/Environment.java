package com.example.framework.config;

import java.util.Map;

public class Environment {
    private String name;
    private String baseURL;
    private String apiBaseURL;
    private int defaultTimeout;
    private Viewport viewport;
    private FeatureFlags featureFlags;

    public Map<String, String> getDefaultHeaders(){
        return Map.of(
                "Content-Type", "application/json"
        );
    }

    public String getName() {
        return name;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getApiBaseURL() {
        return apiBaseURL;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public FeatureFlags getFeatureFlags() {
        return featureFlags;
    }
}
