package com.example.framework.api;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.Environment;
import com.microsoft.playwright.APIResponse;

import java.util.Map;

public class ProductClient extends BaseApiClient {
    public ProductClient(Environment env, ApiSecrets apiSecrets) {
        super(env, apiSecrets);
    }

    public APIResponse getProductById(String productId) {
        return request.get(
                env.getApiBaseURL() + "/products/" + productId,
                withHeaders()
        );
    }

    public APIResponse listProducts() {
        return request.get(
                env.getApiBaseURL() + "/products",
                withHeaders()
        );
    }

    public APIResponse createProduct(String name, String category, double price) {
        return request.post(
                env.getApiBaseURL() + "/products",
                withHeaders().setData(
                        Map.of(
                                "name", name,
                                "category", category,
                                "price", price
                        )
                )
        );
    }

    public APIResponse deleteProduct(String productId) {
        return request.delete(
                env.getApiBaseURL() + "/products/" + productId,
                withHeaders()
        );
    }
}
