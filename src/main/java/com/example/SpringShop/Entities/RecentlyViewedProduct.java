package com.example.SpringShop.Entities;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recently_viewed_products")
public class RecentlyViewedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Customer getCustomer() {return customer;}

    public void setCustomer(Customer customer) {this.customer = customer;}

    public Product getProduct() {return product;}

    public void setProduct(Product product) {this.product = product;}

    public LocalDateTime getViewedAt() {return viewedAt;}

    public void setViewedAt(LocalDateTime viewedAt) {this.viewedAt = viewedAt;}
}
