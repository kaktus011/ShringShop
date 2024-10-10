package com.example.SpringShop.Exceptions;

public class MobileNumberAlreadyExistsException extends RuntimeException {
    public MobileNumberAlreadyExistsException(String mobileNumber) {

        super("Customer with mobile number '" + mobileNumber + "' already exists.");
    }
}
