package com.example.SpringShop.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
