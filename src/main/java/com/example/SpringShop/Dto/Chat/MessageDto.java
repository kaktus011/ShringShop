package com.example.SpringShop.Dto.Chat;

import java.time.LocalDateTime;

public class MessageDto {
    private String senderName;
    private String content;
    private LocalDateTime date;

    public MessageDto(String senderName, String content, LocalDateTime date) {
        this.senderName = senderName;
        this.content = content;
        this.date = date;
    }

    public String getSenderName() {return senderName;}

    public void setSenderName(String senderName) {this.senderName = senderName;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}
}