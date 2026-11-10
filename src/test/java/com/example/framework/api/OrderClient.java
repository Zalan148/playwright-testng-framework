package com.example.framework.api;

import com.example.framework.config.ApiSecrets;
import com.example.framework.config.Environment;
import com.microsoft.playwright.APIResponse;
import java.util.Map;

public class OrderClient extends BaseApiClient {
    public OrderClient(Environment env, ApiSecrets apiSecrets) {
        super(env, apiSecrets);
    }

    public APIResponse getOrderById(String orderId) {
        return request.get(
                env.getApiBaseURL() + "/orders/" + orderId,
                withHeaders()
        );
    }

    public APIResponse createOrder(String userId, String productId, int quantity) {
        return request.post(
                env.getApiBaseURL() + "/orders",
                withHeaders().setData(
                        Map.of(
                                "userId", userId,
                                "productId", productId,
                                "quantity", quantity
                        )
                )
        );
    }

    public APIResponse deleteOrder(String orderId) {
        return request.delete(
                env.getApiBaseURL() + "/orders/" + orderId,
                withHeaders()
        );
    }
}
