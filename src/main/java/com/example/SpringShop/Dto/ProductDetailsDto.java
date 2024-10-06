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
    private String creatorName;
    private String creatorEmail;
    private String creatorPhone;
    private String creatorLocation;


    public ProductDetailsDto() {
    }

    public ProductDetailsDto(long id, String title, String description, double price, String category, List<String> images, int status, String location, long creatorId, String creatorName, String creatorEmail, String creatorPhone, String creatorLocation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.images = images;
        this.status = status;
        this.location = location;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creatorEmail = creatorEmail;
        this.creatorPhone = creatorPhone;
        this.creatorLocation = creatorLocation;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorPhone() {
        return creatorPhone;
    }

    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    public String getCreatorLocation() {
        return creatorLocation;
    }

    public void setCreatorLocation(String creatorLocation) {
        this.creatorLocation = creatorLocation;
    }
}