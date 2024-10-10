package com.example.SpringShop.Exceptions;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException() {

        super("Invalid product.");
    }
}
