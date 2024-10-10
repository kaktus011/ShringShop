package com.example.SpringShop.Exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {

        super("Invalid password.");
    }
}

