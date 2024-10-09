package com.example.SpringShop.Dto;

import java.util.List;

public class ChatDto {
    private Long id;
    private List<MessageDto> messages;

    public ChatDto(Long id, List<MessageDto> messages) {
        this.id = id;
        this.messages = messages;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public List<MessageDto> getMessages() {return messages;}

    public void setMessages(List<MessageDto> messages) {this.messages = messages;}
}
