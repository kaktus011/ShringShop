package com.example.SpringShop.Dto.Product;

public class ProductInCartDto {
    private long id;
    private String title;
    private double price;
    private String image;

    public ProductInCartDto() {}
    public ProductInCartDto(long id, String title, double price, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
