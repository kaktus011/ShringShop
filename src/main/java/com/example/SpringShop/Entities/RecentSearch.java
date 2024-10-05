package com.example.SpringShop.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recent_searches")
public class RecentSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "search_term", nullable = false)
    private String searchTerm;
    @Column(name = "search_date", nullable = false)
    private LocalDateTime searchDate;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
