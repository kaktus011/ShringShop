package com.example.SpringShop.Exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("Customer with username already exists.");
    }
}
