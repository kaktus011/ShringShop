package com.example.SpringShop.Dto;

public class ErrorResponseDto {
    private String message;

    public ErrorResponseDto(String message) {this.message = message;}

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}
