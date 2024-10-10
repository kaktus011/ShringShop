package com.example.SpringShop.Exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {

        super("Customer not found.");
    }
}
