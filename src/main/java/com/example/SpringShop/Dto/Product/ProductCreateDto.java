package com.example.SpringShop.Dto.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductCreateDto {

    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Name must be up to 100 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(max = 4000, message = "Description must be up to 4000 characters.")
    private String description;

    private double price;

    @NotBlank(message = "Category is required.")
    @Size(max = 50, message = "Category must be up to 50 characters.")
    private String category;

    @NotBlank(message = "Status is required.")
    @Size(max = 20, message = "Status must be up to 20 characters.")
    private String status;

    @NotBlank(message = "Location is required.")
    @Size(max = 200, message = "Location must be up to 200 characters.")
    private String location;

    @NotBlank(message = "Image URL is required.")
    private String image;


    public ProductCreateDto() {
    }

    public ProductCreateDto(String title, String description, String location,
                            String status, String image, double price, String category) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.status = status;
        this.image = image;
        this.price = price;
        this.category = category;
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
