package com.example.SpringShop.Exceptions;

public class NewNumberSameLikeOldNumberException extends RuntimeException {
    public NewNumberSameLikeOldNumberException() {

        super("New mobile number should be different from old mobile number.");
    }
}
