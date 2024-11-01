package com.example.SpringShop.Dto.Product;

public class ProductDetailsDto {
    private long productId;
    private String title;
    private String description;
    private double price;
    private Long categoryId;
    private String category;
    private String image;
    private String status;
    private String location;
    private int views;
    private long creatorId;
    private String creatorName;
    private String creatorPhone;
    private String creatorEmail;
    private int customersWhoFavourited;


    public ProductDetailsDto() {
    }


    public ProductDetailsDto(long productId, String title, String description, double price, Long categoryId, String category, String image, String status, String location, int views, long creatorId, String creatorName, String creatorPhone, String creatorEmail, int customersWhoFavourited) {
        this.productId = productId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.category = category;
        this.image = image;
        this.status = status;
        this.location = location;
        this.views = views;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creatorPhone = creatorPhone;
        this.creatorEmail = creatorEmail;
        this.customersWhoFavourited = customersWhoFavourited;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public int getViews() {return  views;}

    public void setViews(int views) {this.views = views; }

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

    public int getCustomersWhoFavourited() {return customersWhoFavourited;}

    public void setCustomersWhoFavourited(int customersWhoFavourited) {}
}