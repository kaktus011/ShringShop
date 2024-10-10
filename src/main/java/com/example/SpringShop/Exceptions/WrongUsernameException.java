package com.example.SpringShop.Exceptions;

public class WrongUsernameException extends RuntimeException {
    public WrongUsernameException() {
        super("Wrong username for user.");
    }
}
