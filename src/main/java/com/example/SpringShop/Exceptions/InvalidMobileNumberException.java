package com.example.SpringShop.Exceptions;

public class InvalidMobileNumberException extends RuntimeException {
  public InvalidMobileNumberException() {

    super("Invalid mobile number.");
  }
}
