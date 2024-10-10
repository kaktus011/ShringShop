package com.example.SpringShop.Exceptions;

public class NewEmailSameLikeOldEmailException extends RuntimeException {
    public NewEmailSameLikeOldEmailException() {

        super("New email should be different from old email");
    }
}
