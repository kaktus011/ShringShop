package com.example.SpringShop.Exceptions;

public class NewCategoryNameSameLikeOldNameException extends RuntimeException {
    public NewCategoryNameSameLikeOldNameException() {

        super("New category name should be different from old category name.");
    }
}
