package com.example.SpringShop.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany(mappedBy = "favouriteProducts")
    private List<Customer> customersWhoFavourited;

    @ManyToOne
    @JoinColumn(name ="category_id", nullable = false)
    private Category category;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getLocation() {return location;}

    public void setLocation(String location) {this.location = location;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public int getView() {return view;}

    public void setView(int view) {this.view = view;}

    public boolean isActive() {return isActive;}

    public void setActive(boolean active) {isActive = active;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public List<Customer> getCustomersWhoFavourited() {return customersWhoFavourited;}

    public void setCustomersWhoFavourited(List<Customer> customersWhoFavourited) {this.customersWhoFavourited = customersWhoFavourited;}

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}

    public LocalDateTime getCreationDate() {return creationDate;}

    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
}


