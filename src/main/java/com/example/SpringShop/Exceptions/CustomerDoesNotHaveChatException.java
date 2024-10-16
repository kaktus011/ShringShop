package com.example.SpringShop.Exceptions;

public class CustomerDoesNotHaveChatException extends RuntimeException {
    public CustomerDoesNotHaveChatException(String message) {

        super(message);
    }
}
