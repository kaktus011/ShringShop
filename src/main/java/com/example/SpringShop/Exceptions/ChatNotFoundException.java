package com.example.SpringShop.Exceptions;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException() {
        super("Chat not found.");
    }
}
