package com.example.SpringShop.Exceptions;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(String message) {

        super(message);
    }
}
