package com.rasheedha.rasheedhamart.model;

import java.sql.Timestamp;

public class Promotion {
    private Long id;
    private String title;
    private String description;
    private String bannerUrl;
    private int discountPercentage;
    private String targetCategory;
    private boolean active;
    private Timestamp createdAt;

    public Promotion() {}

    public Promotion(Long id, String title, String description, String bannerUrl, int discountPercentage, String targetCategory, boolean active, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bannerUrl = bannerUrl;
        this.discountPercentage = discountPercentage;
        this.targetCategory = targetCategory;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }

    public int getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(int discountPercentage) { this.discountPercentage = discountPercentage; }

    public String getTargetCategory() { return targetCategory; }
    public void setTargetCategory(String targetCategory) { this.targetCategory = targetCategory; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
