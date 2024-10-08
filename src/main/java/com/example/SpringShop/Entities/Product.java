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

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "views")
    private int view;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany(mappedBy = "favouriteProducts")
    private List<Customer> customersWhoFavourited;

    @ManyToOne
    @JoinColumn(name ="category_id", nullable = false)
    private Category category;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getView() {
        return view;
    }

    public boolean isActive() {
        return isActive;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Customer> getCustomersWhoFavourited() {
        return customersWhoFavourited;
    }

    public Category getCategory() {
        return category;
    }
}