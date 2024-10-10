package com.example.SpringShop.Exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {

        super("Invalid email.");
    }
}
