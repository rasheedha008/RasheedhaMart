package com.rasheedha.rasheedhamart.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private Long buyerId;
    private Long shippingAddressId;
    private BigDecimal totalAmount;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED
    private Timestamp createdAt;
    private List<OrderItem> items = new ArrayList<>();
    private AddressSnapshot shippingAddressSnapshot;

    public Order() {}

    public Order(Long id, Long buyerId, BigDecimal totalAmount, String status, Timestamp createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order(Long id, Long buyerId, Long shippingAddressId, BigDecimal totalAmount, String status, Timestamp createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.shippingAddressId = shippingAddressId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }

    public Long getShippingAddressId() { return shippingAddressId; }
    public void setShippingAddressId(Long shippingAddressId) { this.shippingAddressId = shippingAddressId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public AddressSnapshot getShippingAddressSnapshot() { return shippingAddressSnapshot; }
    public void setShippingAddressSnapshot(AddressSnapshot shippingAddressSnapshot) { this.shippingAddressSnapshot = shippingAddressSnapshot; }
}
