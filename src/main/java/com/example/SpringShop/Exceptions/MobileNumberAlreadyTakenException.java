package com.example.SpringShop.Exceptions;

public class MobileNumberAlreadyTakenException extends RuntimeException {
    public MobileNumberAlreadyTakenException(String number) {

        super("Mobile number '" + number + "' is already taken.");
    }
}
