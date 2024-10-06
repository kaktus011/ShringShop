package com.example.SpringShop.Dto;

import java.util.List;

public class ProductCreateDto {
    private String title;
    private String description;
    private double price;
    private String category;
    private int status; //new, used
    private String location;
    private List<String> images;
    private long creatorId;

    public ProductCreateDto() {
    }

    public ProductCreateDto(String title, String description, double price, String category, int status, String location) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = status;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
