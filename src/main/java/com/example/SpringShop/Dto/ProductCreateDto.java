package com.example.SpringShop.Dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ProductCreateDto {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Description is required.")

    private String description;

    private double price;

    @NotBlank(message = "Category is required.")
    private String category;

    @NotBlank(message = "Status is required.")
    private String status;

    @NotBlank(message = "Location is required.")
    private String location;

    @NotBlank(message = "Images are required.")
    private String image;


    public ProductCreateDto() {
    }

    public ProductCreateDto(String title, String description, double price, String category, String status, String location, String image) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.status = status;
        this.location = location;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
