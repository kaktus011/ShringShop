package com.example.SpringShop.Exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {

        super("Invalid username or password.");
    }
}
