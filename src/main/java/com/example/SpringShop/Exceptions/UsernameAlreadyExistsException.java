package com.example.SpringShop.Exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username) {
        super("Customer with username '" + username + "' already exists.");
    }
}
