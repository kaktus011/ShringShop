package com.example.SpringShop.Dto;

public class ProductDetailsDto {
    private long id;
    private String title;
    private String description;
    private double price;
    private String category;
    private String image;
    private String status; //new, used
    private String location;
    private long creatorId;
    private String creatorName;
    private String creatorPhone;
    private String creatorEmail;


    public ProductDetailsDto() {
    }


    public ProductDetailsDto(long id, String title, String description, double price, String category, String image, String status, String location, long creatorId, String creatorName, String creatorPhone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.status = status;
        this.location = location;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creatorPhone = creatorPhone;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

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

    public String getCreatorPhone() {
        return creatorPhone;
    }

    public void setCreatorPhone(String creatorPhone) {
        this.creatorPhone = creatorPhone;
    }

    public String getCreatorEmail() {return creatorEmail;}

    public void setCreatorEmail(String creatorEmail) {this.creatorEmail = creatorEmail;}
}