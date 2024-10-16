package com.example.SpringShop.Dto.Chat;

import java.time.LocalDateTime;

public class ChatOverviewDto {
    private Long chatId;
    private String otherPersonName;
    private Long otherPersonId;
    private LocalDateTime lastMessageTime;

    public ChatOverviewDto(Long chatId, String otherPersonName, Long otherPersonId, LocalDateTime lastMessageTime) {
        this.chatId = chatId;
        this.otherPersonName = otherPersonName;
        this.otherPersonId = otherPersonId;
        this.lastMessageTime = lastMessageTime;
    }

    public Long getChatId() {return chatId;}

    public void setChatId(Long chatId) {this.chatId = chatId;}

    public String getOtherPersonName() {return otherPersonName;}

    public void setOtherPersonName(String otherPersonName) {this.otherPersonName = otherPersonName;}

    public Long getOtherPersonId() {return otherPersonId;}

    public void setOtherPersonId(Long otherPersonId) {this.otherPersonId = otherPersonId;}

    public LocalDateTime getLastMessageTime() {return lastMessageTime;}

    public void setLastMessageTime(LocalDateTime lastMessageTime) {this.lastMessageTime = lastMessageTime;}
}
