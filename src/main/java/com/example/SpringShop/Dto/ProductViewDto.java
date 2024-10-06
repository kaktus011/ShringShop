package com.example.SpringShop.Dto;

import java.time.LocalDateTime;

public class ProductViewDto {
    private long id;
    private String title;
    private double price;
    private String location;
    private String image;
    private LocalDateTime createdAt;

    public ProductViewDto() {
    }

    public ProductViewDto(long id, String title, double price, LocalDateTime createdAt, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.createdAt = createdAt;
        this.image = image;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}