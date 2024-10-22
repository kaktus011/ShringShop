package com.example.SpringShop.Exceptions;

public class ProductNotInFavouritesException extends RuntimeException {

    public ProductNotInFavouritesException(String message) {

        super(message);
    }
}