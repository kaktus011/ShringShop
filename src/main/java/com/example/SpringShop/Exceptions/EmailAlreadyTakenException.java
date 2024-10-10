package com.example.SpringShop.Exceptions;

public class EmailAlreadyTakenException extends RuntimeException {
    public EmailAlreadyTakenException(String email) {

        super("Email '" + email + "' is already taken.");
    }
}
