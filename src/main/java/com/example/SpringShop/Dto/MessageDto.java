package com.example.SpringShop.Dto;

import java.time.LocalDateTime;

public class MessageDto {
    private String customerName;
    private String content;
    private LocalDateTime date;

    public MessageDto(String customerName, String content, LocalDateTime date) {
        this.customerName = customerName;
        this.content = content;
        this.date = date;
    }

    public String getCustomerName() {return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}
}