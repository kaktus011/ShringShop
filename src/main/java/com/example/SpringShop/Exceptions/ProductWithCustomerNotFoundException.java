package com.example.SpringShop.Exceptions;

public class ProductWithCustomerNotFoundException extends RuntimeException {
    public ProductWithCustomerNotFoundException() {

        super("Customer does not have access to this product.");
    }
}
