package com.example.SpringShop.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
