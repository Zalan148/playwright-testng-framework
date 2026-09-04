package com.example.framework.models;

public class Order {
    private String id;
    private String userId;
    private String productId;
    private int quantity;
    private String status;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
}
