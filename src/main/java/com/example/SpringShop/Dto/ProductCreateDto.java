package com.example.SpringShop.Dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ProductCreateDto {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Description is required.")

    private String description;
    @NotBlank(message = "Price is required.")
    private double price;

    @NotBlank(message = "Category is required.")
    private String category;

    @NotBlank(message = "Status is required.")
    private int status; //new, used

    @NotBlank(message = "Location is required.")
    private String location;

    @NotBlank(message = "Imgages are required.")
    private List<String> images;

    private String mobileNumber;

    private String email;
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
