package com.example.SpringShop.Dto.Product;

import java.time.LocalDateTime;

public class ProductIndexDto {
    private Long productId;
    private String title;
    private String description;
    private double price;
    private String category;
    private Long categoryId;
    private String status;
    private String location;
    private String image;
    private Long customerId;
    private int views;
    private int customersWhoFavourited;
    private boolean isActive;
    private LocalDateTime creationDate;


    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public Long getCategoryId() {return categoryId;}

    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image;}

    public Long getProductId() {return productId;}

    public void setProductId(Long productId) {this.productId = productId;}

    public Long getCustomerId() {return customerId;}

    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public int getViews() {return views;}

    public void setViews(int views) {this.views = views;}

    public int getCustomersWhoFavourited() {return customersWhoFavourited;}
    public void setCustomersWhoFavourited(int customersWhoFavourited) {}

    public boolean isActive() {return isActive;}

    public void setActive(boolean active) {isActive = active;}

    public LocalDateTime getCreationDate() {return creationDate;}

    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
}
