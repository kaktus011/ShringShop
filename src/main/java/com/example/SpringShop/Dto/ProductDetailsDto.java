package com.example.SpringShop.Dto;

import java.util.List;

public class ProductDetailsDto {
    private long id;
    private String title;
    private String description;
    private double price;
    private String category;
    private List<String> images;
    private int status; //new, used
    private String location;
    private long creatorId;

    public ProductDetailsDto() {
    }

    public ProductDetailsDto(long id, String title, String description, double price, String category, List<String> images, int status, String location, long creatorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.status = status;
        this.location = location;
        this.creatorId = creatorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> image) {
        this.images = image;
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

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}