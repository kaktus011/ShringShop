package com.example.SpringShop.Exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String category) {

        super("Category '" + category + "' not found.");
    }
}
