package com.example.SpringShop.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recent_searches")
public class RecentSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_name", nullable = false)
    private String searchName;

    @Column(name = "search_date", nullable = false)
    private LocalDateTime searchDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Long getId() {
        return id;
    }

    public String getSearchName() {
        return searchName;
    }

    public LocalDateTime getSearchDate() {
        return searchDate;
    }

    public Customer getCustomer() {
        return customer;
    }
}