package com.example.SpringShop.Dto;

import java.util.List;

public class CartViewDto {
    private Long cartId;
    private Long customerId;
    private List<ProductInCartDto> products;
    private double totalPrice;
    private int totalProducts;

    public Long getCartId() {return cartId;}

    public void setCartId(Long cartId) {this.cartId = cartId;}

    public Long getCustomerId() {return customerId;}

    public void setCustomerId(Long customerId) {this.customerId = customerId;}

    public List<ProductInCartDto> getProducts() {return products;}

    public void setProducts(List<ProductInCartDto> products) {this.products = products;}

    public double getTotalPrice() {return totalPrice;}

    public void setTotalPrice(double totalPrice) {this.totalPrice = totalPrice;}

    public int getTotalProducts() {return totalProducts;}

    public void setTotalProducts(int totalProducts) {this.totalProducts = totalProducts;}
}
