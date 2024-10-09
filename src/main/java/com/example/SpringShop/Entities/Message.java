package com.example.SpringShop.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Customer receiver;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Customer getSender() {return sender;}

    public void setSender(Customer sender) {this.sender = sender;}

    public Customer getReceiver() {return receiver;}

    public void setReceiver(Customer receiver) {this.receiver = receiver;}

    public Chat getChat() {return chat;}

    public void setChat(Chat chat) {this.chat = chat;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}
}