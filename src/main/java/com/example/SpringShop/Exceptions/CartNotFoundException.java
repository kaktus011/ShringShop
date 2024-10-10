package com.example.SpringShop.Exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {

        super("Cart not found.");
    }
}
