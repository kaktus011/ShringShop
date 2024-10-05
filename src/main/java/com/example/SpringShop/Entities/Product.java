package com.example.SpringShop.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private String status; //new, old, used with no remarks

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "views")
    private int view;//kolko puti produkta e vidqn

    @Column(name = "is_active")
    private boolean isActive;//dali obqvata za produkt e aktivna ili ne

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany(mappedBy = "favouriteProducts")
    private List<Customer> customersWhoFavourited;

    @ManyToOne
    @JoinColumn(name ="category_id", nullable = false)
    private Category category;
}
