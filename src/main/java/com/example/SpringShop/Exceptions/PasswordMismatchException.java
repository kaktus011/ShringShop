package com.example.SpringShop.Exceptions;

public class PasswordMismatchException extends RuntimeException {
  public PasswordMismatchException() {

    super("New password should be different from the old password.");
  }
}
