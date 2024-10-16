package com.example.SpringShop.Exceptions;

public class CategoryAlreadyTakenException extends RuntimeException {
    public CategoryAlreadyTakenException(String category) {

      super("Category name '" + category + "' is already taken.");
    }
}
