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

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

}