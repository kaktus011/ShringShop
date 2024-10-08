package com.example.SpringShop.Dto;

public class ProductViewDto {
    private long id;
    private String title;
    private double price;
    private String location;
    private String image;

    public ProductViewDto(long id, String title, double price, String image, String location) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.location = location;
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


}