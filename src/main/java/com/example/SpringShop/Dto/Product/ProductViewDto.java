package com.example.SpringShop.Dto.Product;

import java.time.LocalDateTime;

public class ProductViewDto {
    private Long id;
    private String title;
    private double price;
    private String location;
    private String image;
    private LocalDateTime createdAt;

    public ProductViewDto() {}
    public ProductViewDto(long id, String title, double price, String image, String location, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.location = location;
        this.createdAt = createdAt;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}