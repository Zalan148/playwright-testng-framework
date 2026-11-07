package com.example.framework.api;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.Environment;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;


public class BaseApiClient {
    protected final Environment env;
    protected final APIRequestContext request;
    protected final ApiSecrets apiSecrets;

    public BaseApiClient(Environment env, ApiSecrets apiSecrets) {
        this.env = env;
        this.apiSecrets = apiSecrets;
        Playwright playwright = Playwright.create();
        // Version only supports this simple context creation
        this.request = playwright.request().newContext();
    }

    protected RequestOptions withHeaders() {
        RequestOptions options = RequestOptions.create();

        // Add default environment headers
        env.getDefaultHeaders().forEach(options::setHeader);

        // Add API secrets (token, admin creds, etc.)
        options.setHeader("Authorization", "Bearer " + apiSecrets.getApiToken());
        return options;
    }

}
