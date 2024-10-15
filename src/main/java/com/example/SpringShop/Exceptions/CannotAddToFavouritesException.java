package com.example.SpringShop.Exceptions;

public class CannotAddToFavouritesException extends RuntimeException {
    public CannotAddToFavouritesException(String message) {

        super(message);
    }
}
